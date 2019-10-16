import numpy as np
import scipy as sp
import scipy.stats as stats
import matplotlib.pyplot as plt
import torch
import torchvision

data = np.load('reg_data_noise.npy')

xs = data[:, 0]
ys = data[:, 1]

plt.plot(xs, ys, '.')


# model = torch.nn.Sequential(torch.nn.Linear(1, 50),
#                             torch.nn.Tanh(), torch.nn.Linear(50, 50),
#                             torch.nn.Tanh(), torch.nn.Linear(50, 1))
model = torch.load("regression.pt")

t_xs = torch.from_numpy(xs).view(-1, 1)
t_ys = torch.from_numpy(ys).view(-1, 1)

t_out = model(t_xs)

plt.plot(xs, t_out.data.view(-1).numpy(), '.')

dataset = torch.utils.data.TensorDataset(t_xs, t_ys)
(train_set, valid_set) = torch.utils.data.random_split(dataset, (160, 40))

train_loader = torch.utils.data.DataLoader(train_set, batch_size=len(train_set))

optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

loss_func = torch.nn.MSELoss()
err_train = []
err_valid = []

model.to('cuda')

for epoch in range(30000):
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
    if epoch % 1000 == 0:
        print("epoch %d %f %f " % (epoch, loss, vloss))

plt.plot(err_train, '.', label='train')
plt.plot(err_valid, '.', label='valid')
plt.legend()
plt.show()

model.to("cpu")
t_out_train = model(train_set[:][0])
plt.plot(train_set[:][0].data.numpy(), t_out_train.data.view(-1).numpy(), '.')
t_out_valid = model(valid_set[:][0])
plt.plot(valid_set[:][0].data.numpy(), t_out_valid.data.view(-1).numpy(), '.')
plt.show()

torch.save(model, "regression.pt")
