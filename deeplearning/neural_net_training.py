import numpy as np
import scipy as sp
import scipy.stats as stats
import matplotlib.pyplot as plt
import torch
import torchvision

data = np.load('2/reg_multi.npy')
data.shape
x = data[:, :16]
y = data[:, 16]

model = torch.nn.Sequential(torch.nn.Linear(16, 100),
                            torch.nn.Tanh(), torch.nn.Linear(100, 100),
                            torch.nn.ReLU(), torch.nn.Linear(100, 100),
                            torch.nn.Tanh(), torch.nn.Linear(100, 1))

t_xs = torch.from_numpy(x).view(-1, 16)
t_ys = torch.from_numpy(y).view(-1, 1)

dataset = torch.utils.data.TensorDataset(t_xs, t_ys)
(train_set, valid_set) = torch.utils.data.random_split(dataset, (55000, 5000))

train_loader = torch.utils.data.DataLoader(train_set, batch_size=10000,drop_last=True)

optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

loss_func = torch.nn.MSELoss()
err_train = []
err_valid = []

model.to('cuda')

for epoch in range(100):
    for datum in train_loader:
        optimizer.zero_grad()
        (features, target) = datum
        pred = model(features.to('cuda'))
        loss = loss_func(pred, target.to('cuda'))
        loss.backward()
        optimizer.step()

    with torch.no_grad():
        vpred = model(valid_set[:][0].to('cuda'))
        vloss = loss_func(vpred, valid_set[:][1].to('cuda'))
        err_valid.append(vloss)
        pred = model(train_set[:][0].to('cuda'))
        loss = loss_func(pred, train_set[:][1].to('cuda'))
        err_train.append(loss)
    print("epoch %d %f %f " % (epoch, loss, vloss))

plt.plot(err_train, label='train')
plt.plot(err_valid, label='valid')
plt.legend()
plt.show()

torch.save(model, "multi_regression.pt")
