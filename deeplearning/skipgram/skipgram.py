import numpy as np
import re
import random
import torch
import torch.nn as nn
import torch.nn.functional as F

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


def checkfornumber(string):
    return bool(re.search(r'[0-9]',string))


pol = []
with open("pol.txt", 'r') as f:
    text = f.readlines()
    for string in text:
        en, pl = string.split('\t')[:2]
        if not (checkfornumber(pl)):

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

pl = list(map(polish.sentencetoindex,pol))

len(pl)