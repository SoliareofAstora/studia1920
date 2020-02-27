import numpy as np
import re
import random
import torch
import torch.nn as nn
import torch.nn.functional as F

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


def checkfornumber(string):
    return bool(re.search(r'[0-9]',string))


eng = []
pol = []
with open("pol.txt", 'r') as f:
    text = f.readlines()
    for string in text:
        en, pl = string.split('\t')[:2]
        if not (checkfornumber(en+pl))\
                and len(en.split(' '))<10\
                and len(en.split(' '))<10:

            eng.append(en)
            pol.append(pl)

interpunction = [',','.','?','!']


def split_sentence(sentence):
    words = []
    words.append("SOS")
    for word in sentence.split(" "):
        if word[-1] in interpunction:
            words.append(word[:-1])
            words.append(word[-1])
        else:
            words.append(word)
    words.append("EOS")
    return words


class Language(object):
    def __init__(self, data):
        self.word2index = {}
        self.index2word = {}
        self.addWord("SOS")
        self.addWord("EOS")
        self.addlist(data)

    def addlist(self,input_list):
        for sentence in input_list:
            for word in split_sentence(sentence):
                self.addWord(word)

    def addWord(self,word):
        if word not in self.word2index:
            index = len(self.word2index)
            self.word2index[word] = index
            self.index2word[index]=word

    def __len__(self):
        return len(self.word2index)

    def sentencetoindex(self, sentence):
        output = []
        for word in split_sentence(sentence):
            output.append(self.word2index[word])
        return torch.LongTensor(output).to(device)

    def indextosentence(self, index):
        output = []
        for i in index:
            output.append(self.index2word[i])
        return " ".join(output)

polish = Language(pol)
english = Language(eng)

pl = list(map(polish.sentencetoindex,pol))
en = list(map(english.sentencetoindex,eng))

len(pl)
len(en)


class encoder(nn.Module):
    def __init__(self, input_size, hidden_size):
        super(encoder, self).__init__()
        self.hidden_size = hidden_size
        self.embedding = nn.Embedding(input_size, hidden_size)
        self.lstm = nn.LSTMCell(hidden_size, hidden_size)
        self.init_hidden = 2*[torch.zeros(1, hidden_size, device=device)]

    def forward(self, input):
        hidden = self.init_hidden
        for i in input:
            x = self.embedding(i).view(1,-1)
            hidden = self.lstm(x, hidden)
        return hidden


class decoder(nn.Module):
    def __init__(self, hidden_size, output_size):
        super(decoder, self).__init__()
        self.embedding = nn.Embedding(output_size, hidden_size)
        self.lstm = nn.LSTMCell(hidden_size, hidden_size)
        self.out = nn.Linear(hidden_size, output_size)
        self.softmax = nn.LogSoftmax(dim=0)

    def forward(self, input, hidden):
        output = self.embedding(input).view(1, -1)
        output = F.relu(output)
        hidden = self.lstm(output, hidden)
        output = self.softmax(self.out(output[0]))
        return output, hidden


class Model(nn.Module):
    def __init__(self, input_size, hidden_size, output_size):
        super(Model, self).__init__()
        self.encoder = encoder(input_size,hidden_size)
        self.decoder = decoder(hidden_size,output_size)

    def forward(self, sentence, truetranslation=None, help_parameter=-1):
        hidden = self.encoder(sentence)
        output = torch.zeros(1,dtype=torch.int64).to(device)
        sentence = [output[0]]
        outputs = []
        # max_len = len(sentence)+15 if truetranslation is None else len(truetranslation)+5
        max_len = 13
        length = 1
        while output.max(0)[1].detach().cpu().numpy() != 1 and length < max_len:
            output, hidden = self.decoder(output.max(0)[1],hidden)
            length += 1
            outputs.append(output)
            sentence.append(output.max(0)[1])
            if random.random()<help_parameter:
                output = torch.zeros_like(output)
                if length < len(truetranslation):
                    output[truetranslation[length]]=1
                else:
                    output[1]=1
        return sentence, outputs


def get_sentence(vector):
    vector = torch.stack(vector).detach().cpu().numpy()
    return english.indextosentence(vector)


data = np.arange(0,len(pl))
en[1000]
eng[1000]
index = len(eng)-1
model = Model(len(polish),3000,len(english)).to(device)
optimizer = torch.optim.Adam(model.parameters(), 0.001)
criterion = nn.NLLLoss()
batch_update = 128
n_batch = 0

loss = 0

benchmark = 34499
print('English:',pol[benchmark])
print("Translation:",eng[benchmark])

help_param = 1


for epoch in range(10):

    np.random.shuffle(data)
    losses = []
    for index in data:


        input = pl[index]
        original = en[index][1:].view(-1,1)
        sentence, smf = model(input,original,help_param)
        help_param -= help_param / 20000*len(sentence)
        smf = torch.stack(smf)
        true = original
        if len(original) < len(smf):
            true = torch.cat([original, torch.ones([smf.shape[0] - original.shape[0], 1], dtype=torch.int64).to(device)])
        else:
            if len(original) > len(smf):
                smf = torch.cat([smf, torch.stack([smf[-1]] * (len(original) - len(smf)))])
                # true = original[:len(smf)]

        multi = 1
        if sentence[1] == 1:
            # print("Za mało wypowiedzi")
            multi = 10
        if torch.stack(sentence).sum()==0:
            # print("Za dużo sosu")
            multi = 10

        loss += multi * criterion(
            smf,
            true.view(-1)
        )

        n_batch+=1
        if n_batch>batch_update:
            losses.append(loss.item())
            loss.backward()
            optimizer.step()

            # print('\t',help_param,'loss', int(np.average(losses)), "Translation =", get_sentence(model(pl[benchmark])[0]))
            optimizer.zero_grad()
            n_batch = 0
            loss = 0

    print("epoch", epoch, 'loss',int(np.average(losses)),"Translation =", get_sentence(model(pl[benchmark])[0]))
torch.save(model.state_dict(),'seq2seq.h5')