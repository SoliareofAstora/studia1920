# Explore the dataset and decide on the research question. For example you can make a regression of CO2 emissions
# based on energy consuption and other factors. Additionally, you might wish to check if there have been any visible
# changes in and any correlations between the size of arable land, cereal yield, or droughts in the last decades. Can
# you make any predictions?


import pandas as pd
import pathlib
import numpy as np
import matplotlib.pyplot as plt


path = pathlib.Path("data")
climateChange = list(path.glob('*climate-change*'))
indicators = list(path.glob("*indicators*"))
WDI = list(path.glob("WDI*"))

len(climateChange)
len(indicators)
len(WDI)

climate = [pd.read_csv(file, index_col=None, header=1) for file in climateChange]
indicator = [pd.read_csv(file, index_col=None, header=1) for file in indicators]
final = pd.concat(climate+indicator)

final = final.rename(
    columns={'#country+name': 'name', '#country+code': 'code', '#date+year': 'year', '#indicator+name': 'indicator',
             '#indicator+code': 'indicator_code', '#indicator+value+num': 'value'})

types = np.unique(final.indicator, return_counts=True)
for i in range(len(types[0])):
    print(types[0][i], types[1][i])
for i in np.argsort(types[1]):
    print(types[0][i], types[1][i])


    

types = np.unique(final.name, return_counts=True)
for i in range(len(types[0])):
    print(types[0][i], types[1][i])

types = np.unique(final.year, return_counts=True)
for i in range(len(types[0])):
    print(types[0][i], types[1][i])

plt.plot(types[0],types[1])
plt.show()

