import pickle
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import nltk
import csv
import pandas as pd
from sentence_transformers import SentenceTransformer
import tqdm as notebook_tqdm
from sklearn.model_selection import train_test_split
from sklearn import svm
from sklearn.metrics import f1_score, classification_report, accuracy_score
import time


start_time = time.time()
with open('src\main\java\DigitalAssistant\skillLogic\BERT\embeddings_sentences_training.pickle', 'rb') as pkl:
    embeddings_sentences_training = pickle.load(pkl)

with open('src\main\java\DigitalAssistant\skillLogic\BERT\y_train.pickle', 'rb') as pkl2:
    y_train = pickle.load(pkl2)


model = SentenceTransformer('bert-base-nli-mean-tokens')

classifier = svm.SVC(kernel='rbf', C=8, probability=True)
classifier.fit(embeddings_sentences_training, y_train)

skills = {
    1: 'DistanceFinder',
    2: 'MariekeSkill',
    3: 'Exams',
    4: 'Subjects',
    5: 'Timer',
    6: 'CFGTest',
    7: 'Translator',
    8: 'OpeningTimes',
    9: 'Fixing'
}

with open('src\main\java\DigitalAssistant\skillLogic\BERT\BERT_Input.txt', 'r') as r:
    sentence = r.readline()
print(sentence)
sentence = pd.Series(sentence)
encoded_sentence = model.encode(sentence)

predictedStr = skills[classifier.predict(encoded_sentence)[0]]

print(predictedStr)

with open('src\main\java\DigitalAssistant\skillLogic\BERT\BERT_Output.txt', 'w') as f:
    f.write(predictedStr)

print("--- %s seconds ---" % (time.time() - start_time))


