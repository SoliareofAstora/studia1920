import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import string
from sklearn.linear_model import LogisticRegression

def remove_punctuation(text):
    import string
    translator = str.maketrans('', '', string.punctuation)
    return str(text).translate(translator)

baby_df = pd.read_csv('amazon_baby.csv')
baby_df.head()

baby_df['review'] = baby_df["review"].apply(remove_punctuation)
baby_df['review'] = baby_df["review"].apply(lambda x: '' if x=='nan' else x)

baby_df = baby_df[baby_df["rating"]!=3]

baby_df['rating'] = baby_df["rating"].apply(lambda x: -1 if x<3 else 1)
baby_df = baby_df.reset_index()

from sklearn.feature_extraction.text import CountVectorizer

vectorizer = CountVectorizer()
reviews_train_example = ["We like apples",
                   "We hate oranges",
                   "I adore bananas",
                   "We like like apples and oranges",
                   "They dislike bananas"]

X_train_example = vectorizer.fit_transform(reviews_train_example)

print(vectorizer.get_feature_names())
print(X_train_example.todense())

reviews_test_example = ["They like bananas",
                   "We hate oranges bananas and apples",
                   "We love bananas"] #New word!

X_test_example = vectorizer.transform(reviews_test_example)

print(X_test_example.todense())

sum(baby_df["rating"] == 1)
sum(baby_df["rating"] == -1)


vectors = vectorizer.fit_transform(baby_df.review)
negative = vectors[baby_df.index[baby_df["rating"] == -1]]
positive = vectors[baby_df.index[baby_df["rating"] == 1]]

from scipy.sparse import vstack
X_train = vstack([negative[2600:],
                positive[10000:]])
X_test = vstack([negative[:2600],
                positive[:10000]])

Y_test = np.array([-1]*2600+[1]*10000)
Y_train = np.array([-1]*(negative.shape[0]-2600)+[1]*(positive.shape[0]-10000))

model = LogisticRegression()
model.fit(X_train,Y_train)

weights = model.coef_
ind = np.argpartition(weights[0], 10)[:10]
print('worst',np.array(vectorizer.get_feature_names())[ind])

ind = np.argpartition(weights[0], -10)[-10:]
print('the best',np.array(vectorizer.get_feature_names())[ind])


y_pred = model.predict(X_test)
print(y_pred)

prob = model.predict_proba(X_test)
print(prob)

def get_string(index):
    if index<2600:
        return baby_df.review[baby_df["rating"] == -1].iloc[index]
    else:
        return baby_df.review[baby_df["rating"] == 1].iloc[index]

ind = np.argpartition(prob[:,0], -10)[-10:]
for id in ind:
    print('bad ',get_string(id))

print()
ind = np.argpartition(prob[:,1], -10)[-10:]
for id in ind:
    print('good ',get_string(id))



from sklearn.metrics import accuracy_score
print(accuracy_score(Y_test,y_pred))
