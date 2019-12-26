import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import cm

house_df = pd.read_csv('kc_house_data.csv')
X = house_df.sqft_living.values.reshape(-1,1)[:100]
y = house_df.price.values.reshape(-1,1)[:100]
print(np.shape(X))
print(np.shape(y))

#a)
def predict(b, a):
    return a.dot(b)

def cost(y_pred, y_true):
    return np.sum(np.power(y_pred-y_true,2))/len(y_pred)



#short test:
test_b = np.array([[1],[1],[1]])
test_X = np.array([[1,2,3],[2,5,4],[3,4,5],[4,5,7]])
test_y = np.array([[1],[2],[3],[4]])

print(predict(test_b, test_X))
print(cost(predict(test_b, test_X),test_y))

scores = []
tests = [i for i in range(-1000, 1001)]
for i in tests:
    scores.append(cost(predict(i, X), y))

plt.plot(range(-1000, 1001), scores)
plt.show()

w = tests[np.argmin(np.array(scores))]
plt.scatter(X, y)
lin = [np.min(X), np.max(X)]
plt.plot(lin, [w * l for l in lin])
plt.show()


X = np.concatenate([X,np.ones_like(X)],axis=1)
# b)
y_shifted = y + 1e6

# No Bias Part
b_arr = [np.array(b1) for b1 in np.linspace(-1000, 1000, num=2001)]
cost_arr = [cost(predict(b, X[:, 0]), y_shifted) for b in b_arr]
best_b1 = b_arr[np.argmin(cost_arr)]

# Bias Part
nb_of_bs = 101  # compute the cost nb_of_bs times in each dimension
b1 = np.linspace(-500, 2000, num=nb_of_bs)  # slope coefficient
b2 = np.linspace(-1e5, 3e6, num=nb_of_bs)  # bias
b_x, b_y = np.meshgrid(b1, b2)  # generate grid
cost_arr_2d = np.zeros((nb_of_bs, nb_of_bs))  # initialize cost matrix

# Fill the cost matrix for each combination of coefficients
for i in range(nb_of_bs):
    for j in range(nb_of_bs):
        cost_arr_2d[i, j] = cost(predict(np.array([[b_x[i, j]], [b_y[i, j]]]), X), y_shifted)

plt.figure(figsize=(15, 20))

plt.subplot(2, 1, 1)
plt.title("Cost heatmap for bias and slope")

plt.contourf(b_x, b_y, np.log(cost_arr_2d), 20, alpha=0.9, cmap=cm.pink)
cbar = plt.colorbar()
plt.scatter(best_b1, 0, label="Best solution without bias")
cbar.ax.set_ylabel('log(cost)')
plt.xlabel("b1 (slope)")
plt.ylabel("b2 (bias)")
plt.legend()

plt.subplot(2, 1, 2)

plt.scatter(X.T[0], y_shifted)
plt.xlabel("living sqft")
plt.ylabel("price")

x_model = np.linspace(np.min(X), np.max(X), 1000)
y_model = best_b1 * x_model
plt.plot(x_model, y_model, label='No Bias. Best b1 = {}, error = {:.2E}'.format(best_b1, cost_arr[np.argmin(cost_arr)]))

best_b1_2d_ind, best_b2_2d_ind = np.unravel_index(cost_arr_2d.argmin(), cost_arr_2d.shape)
best_b1 = b_x[best_b1_2d_ind, best_b2_2d_ind]
best_b2 = b_y[best_b1_2d_ind, best_b2_2d_ind]

y_model = best_b1 * x_model + best_b2
plt.plot(x_model, y_model, label='Best b1 = {}, b2 = {}, error = {:.2E}'.format(best_b1, best_b2, cost_arr_2d[
    best_b1_2d_ind, best_b2_2d_ind]))

plt.legend()

plt.show()