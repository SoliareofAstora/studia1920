import numpy as np
import scipy
import scipy.stats
import torch as t

import matplotlib.pyplot as plt
from IPython.display import clear_output, display

from torch.nn import Sequential, Linear, ReLU, LeakyReLU, Dropout, Sigmoid

if t.cuda.is_available():
    if t.cuda.device_count() > 1:
        device = t.device('cuda:1')
    else:
        device = t.device('cuda')
else:
    device = t.device('cpu')

angle = np.random.uniform(-np.pi, np.pi, (1000, 1)).astype('float32')
data = np.concatenate((np.cos(angle), np.sin(angle)), axis=1)

plt.scatter(data[:, 0], data[:, 1])


data_t = t.from_numpy(data)

data_t


discriminator = Sequential(
    Linear(2, 50),
    ReLU(),
    Linear(50, 100),
    ReLU(),
    Linear(100, 100),
    ReLU(),
    Linear(100, 50),
    ReLU(),
    Linear(50, 1),
    Sigmoid())  # dummy discriminator: please subsitute you own implementation

discriminator.to(device)

generator = Sequential(
    Linear(2, 50),
    ReLU(),
    Linear(50, 100),
    ReLU(),
    Linear(100, 100),
    ReLU(),
    Linear(100, 50),
    ReLU(),
    Linear(50, 2),
)
generator.to(device)

out_t = generator(t.empty(1000, 2, device=device).uniform_(-1, 1));

plt.scatter(out_t.data.cpu().numpy()[:, 0], out_t.data.cpu().numpy()[:, 1])

d_optimizer = t.optim.Adam(discriminator.parameters(), lr=0.0002)

g_optimizer = t.optim.Adam(generator.parameters(), lr=0.0002)

lossfn = t.nn.BCELoss().cuda()
batch_size = 100

for epoch in range(10000):
    angle = np.random.uniform(-np.pi, np.pi, (batch_size, 1)).astype('float32')
    real = t.cuda.FloatTensor(t.from_numpy(np.concatenate((np.cos(angle), np.sin(angle)), axis=1)).to(device))

    noise = t.cuda.FloatTensor(t.empty(batch_size, 2, device=device).uniform_(-1, 1))
    fake_points = generator(noise)

    valid = t.ones([batch_size, 1]).to(device)
    fake = t.zeros([batch_size, 1]).to(device)

    g_optimizer.zero_grad()
    gloss = lossfn(discriminator(fake_points), valid)
    gloss.backward()
    g_optimizer.step()

    d_optimizer.zero_grad()
    dloss_real = lossfn(discriminator(real), valid)
    dloss_fake = lossfn(discriminator(fake_points.detach()), fake)
    dloss = (dloss_fake + dloss_real) / 2
    dloss.backward()
    d_optimizer.step()

    if epoch % 100 == 0:
        print("[Epoch %d] [D loss: %f] [G loss: %f]" % (epoch, dloss.item(), gloss.item()))
        real = real[:100].detach().cpu().numpy()
        fake_points = fake_points[:100].detach().cpu().numpy()
        plt.scatter(real[:, 0], real[:, 1])
        plt.scatter(fake_points[:, 0], fake_points[:, 1])
        plt.show()

epoch+=1


t.save(generator.state_dict(),'./generator.h5')
t.save(discriminator.state_dict(),'./discriminator.h5')


batch_size = 1000
angle = np.random.uniform(-np.pi, np.pi, (batch_size, 1)).astype('float32')
real = t.cuda.FloatTensor(t.from_numpy(np.concatenate((np.cos(angle), np.sin(angle)), axis=1)).to(device))

noise = t.cuda.FloatTensor(t.empty(batch_size, 2, device=device).uniform_(-1, 1))
fake_points = generator(noise)

real = real.detach().cpu().numpy()
fake_points = fake_points.detach().cpu().numpy()
plt.scatter(real[:, 0], real[:, 1])
plt.scatter(fake_points[:, 0], fake_points[:, 1])
plt.show()

#
# for epoch in range(100):
#     for datum in train_loader:
#         optimizer.zero_grad()
#         (features, target) = datum
#         pred = model(features.to('cuda'))
#         loss = loss_func(pred, target.to('cuda'))
#         loss.backward()
#         optimizer.step()
#
#     with torch.no_grad():
#         vpred = model(valid_set[:][0].to('cuda'))
#         vloss = loss_func(vpred, valid_set[:][1].to('cuda'))
#         err_valid.append(vloss)
#         pred = model(train_set[:][0].to('cuda'))
#         loss = loss_func(pred, train_set[:][1].to('cuda'))
#         err_train.append(loss)


