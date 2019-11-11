import numpy as np
import scipy as sp
import scipy.stats as stats
import matplotlib.pyplot as plt
import torch
import torchvision

data = np.load('3/classification_problem.npy')
data.shape

x = data
y = np.concatenate([np.zeros(1000), np.ones(1000), np.full(1000, 2)])

x = (x - x.mean(axis=0))/x.std(axis=0)

model = torch.nn.Sequential(
    torch.nn.Linear(12, 25),
    torch.nn.ReLU(), torch.nn.Linear(25, 50),
    torch.nn.ReLU(), torch.nn.Linear(50, 3),
    torch.nn.Softmax())
# model = torch.load("multi_regression.pt")


t_xs = torch.from_numpy(x).view(-1, 12)
t_ys = torch.from_numpy(y).view(-1, 1).long()

dataset = torch.utils.data.TensorDataset(t_xs, t_ys)
(train_set, valid_set) = torch.utils.data.random_split(dataset, (2000, 1000))

train_loader = torch.utils.data.DataLoader(train_set, batch_size=500, drop_last=True)

optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

loss_func = torch.nn.CrossEntropyLoss()
err_train = []
err_valid = []
train_acc = []
valid_acc = []

model.to('cuda')

for epoch in range(200):
    for datum in train_loader:
        optimizer.zero_grad()
        (features, target) = datum
        pred = model(features.to('cuda'))
        loss = loss_func(pred, target.to('cuda').squeeze(-1))
        loss.backward()
        optimizer.step()

    with torch.no_grad():
        vpred = model(valid_set[:][0].to('cuda'))
        vloss = loss_func(vpred, valid_set[:][1].to('cuda').squeeze(-1))
        _, predicted = torch.max(vpred.data, 1)
        valid_acc.append((predicted == valid_set[:][1].to('cuda').squeeze(-1)).sum().item() / valid_set[:][1].shape[0])
        err_valid.append(vloss)
        pred = model(train_set[:][0].to('cuda'))
        loss = loss_func(pred, train_set[:][1].to('cuda').squeeze(-1))
        _, predicted = torch.max(pred.data, 1)
        train_acc.append((predicted == train_set[:][1].to('cuda').squeeze(-1)).sum().item() / train_set[:][1].shape[0])
        err_train.append(loss)
    print("epoch %d tr loss %f val loss %f tr acc %f val acc %f " % (epoch, loss, vloss, train_acc[-1], valid_acc[-1]))

plt.plot(err_train, label='train loss')
plt.plot(err_valid, label='valid loss')
plt.legend()
plt.show()
plt.plot(valid_acc, label = 'validation accuracy')
plt.plot(train_acc, label="train Accuracy")
plt.legend()
plt.show()

torch.save(model, "3/multi_regression.pt")
