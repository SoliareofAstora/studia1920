import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

wiki = pd.read_csv('people_wiki.csv')
wiki.head()


from sklearn.feature_extraction.text import CountVectorizer
vectorizer = CountVectorizer(max_features=10000, token_pattern=r"(?u)\b\w+\b")
WCmatrix = vectorizer.fit_transform(wiki.text)

from sklearn.metrics import pairwise_distances

wiki['BO-eucl'] = pairwise_distances(WCmatrix,WCmatrix[35817])
wiki.sort_values('BO-eucl')

bo = wiki.index[wiki.name=='Barack Obama'].tolist()
gwb = wiki.index[wiki.name=='George W. Bush'].tolist()
jb = wiki.index[wiki.name=='Joe Biden'].tolist()

pairwise_distances(WCmatrix[gwb],WCmatrix[bo])
pairwise_distances(WCmatrix[jb],WCmatrix[bo])
min = pairwise_distances(WCmatrix[gwb],WCmatrix[jb])
min

def top_words(name):
    """
    Get a table of the most frequent words in the given person's wikipedia page.
    """
    text = np.unique(wiki.text[wiki.name==name].tolist()[0].split(),return_counts=True)
    df = pd.DataFrame({'count':text[1].tolist()}, index=text[0].tolist())
    return df.sort_values(by='count', ascending=False)

obama_words = top_words('Barack Obama')
obama_words

barrio_words = top_words('Francisco Barrio')
barrio_words


common_words = obama_words.join(barrio_words, how='inner',lsuffix="_obama", rsuffix="_Barrio")
common_words.sort_values(by='count_Barrio', ascending=False).head(5)
frequent_words = common_words.sort_values(by='count_obama', ascending=False).head(15).index.tolist()

bush_words = top_words('George W. Bush')
common_words = obama_words.join(bush_words, how='inner',lsuffix="_obama", rsuffix="_Bush")
common_words.sort_values(by='count_obama', ascending=False).head(10)

word_to_ind = {v: i for i, v in enumerate(vectorizer.get_feature_names())}


def checkifallwords(string):
    string = string.split()
    for i in frequent_words:
        if i in string:
            pass
        else:
            return False
    return True

#Problem is with that to is also today tomorrow and toto africa
articles = np.array(list(map(checkifallwords, wiki.text)))
articles.sum()
wiki[articles]['name']


# We could use:
    # from sklearn.feature_extraction.text import TfidfVectorizer
# but since we already know how to compute CountVectorizer, let's use:
from sklearn.feature_extraction.text import TfidfTransformer

vectorizer = CountVectorizer(token_pattern=r"(?u)\b\w+\b")
WCmatrix=vectorizer.fit_transform(wiki.text)# Your code goes here

tfidf=TfidfTransformer(smooth_idf=False, norm=None)
TFIDFmatrix = tfidf.fit_transform(WCmatrix)

wiki['BO-eucl-TF-IDF'] = pairwise_distances(TFIDFmatrix,TFIDFmatrix[35817])
wiki['BO-eucl-TF-IDF'].sort_values( ascending=True).head(10)


name = 'Barack Obama'
word_to_ind = {v: i for i, v in enumerate(vectorizer.get_feature_names())}
def top_words_tf_idf(name):
    """
    Get a table of the largest tf-idf words in the given person's wikipedia page.
    """
    # Your code goes here
    text = np.unique(wiki.text[wiki.name == name].tolist()[0].split())
    tmp = np.array(TFIDFmatrix[wiki[wiki.name == name].index].todense())
    df = pd.DataFrame({'tf-idf': tmp[tmp>0]}, index=text.tolist())
    return df.sort_values(by='tf-idf', ascending=False)



obama_words = top_words_tf_idf('Barack Obama')
obama_words
barrio_words = top_words_tf_idf('Phil Schiliro')
barrio_words

common_words = obama_words.join(barrio_words, how='inner',lsuffix="_obama", rsuffix="_Barrio")
common_words.sort_values(by='tf-idf_obama', ascending=False).head(15)

frequent_words = common_words.sort_values(by='tf-idf_obama', ascending=False).head(15).index.tolist()

bush_words = top_words_tf_idf('George W. Bush')
common_words = obama_words.join(bush_words, how='inner',lsuffix="_obama", rsuffix="_Bush")
common_words.sort_values(by='tf-idf_obama', ascending=False).head(10)

def checkifallwords(string):
    string = string.split()
    for i in frequent_words:
        if i in string:
            pass
        else:
            return False
    return True

#Problem is with that to is also today tomorrow and toto africa
articles = np.array(list(map(checkifallwords, wiki.text)))
articles.sum()
wiki[articles]['name']


jb = wiki.index[wiki.name=='Joe Biden'].tolist()
pairwise_distances(TFIDFmatrix[jb],TFIDFmatrix[35817])


def compute_length(row):
    return len(row.split())

wiki['length'] = list(map(compute_length,wiki.text))# Your code goes here

data = wiki.sort_values(by='BO-eucl-TF-IDF',ascending=True)[['name','length','BO-eucl-TF-IDF']][0:100]
data.head(100)


tweet = pd.DataFrame({'text': ['democratic governments control law in response to popular act']})

text = np.unique(tweet.text.tolist()[0].split())
tmp = np.array(tfidf.transform(vectorizer.transform(tweet.text)).todense())
df = pd.DataFrame({'tf-idf': tmp[tmp > 0]}, index=text.tolist())
df.sort_values(by='tf-idf', ascending=False)

obama = np.array(tfidf.transform(vectorizer.transform(wiki[wiki.name=='Barack Obama'].text)).todense())

from sklearn.metrics.pairwise import cosine_distances # for one pair of samples we can just use this function
cosine_distances(tmp,obama)


wiki['BO-TF-IDF cox'] = cosine_distances(TFIDFmatrix,obama)
data = wiki.sort_values(by='BO-TF-IDF cox',ascending=True)[['name','length','BO-TF-IDF cox']][0:100]
data.head(23)

plt.hist(data['length'],bins=20)
plt.show()
