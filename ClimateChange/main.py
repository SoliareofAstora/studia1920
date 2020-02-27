# Explore the dataset and decide on the research question. For example you can make a regression of CO2 emissions
# based on energy consuption and other factors. Additionally, you might wish to check if there have been any visible
# changes in and any correlations between the size of arable land, cereal yield, or droughts in the last decades.
# Can you make any predictions?

# Kraje bloku wschodniego a zachodniego


import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv("Final_Dataset.csv")
countries_info = pd.read_csv('data/WDICountry.csv')

european = ['Russian Federation', 'Ukraine', 'France', 'Spain', 'Sweden', 'Norway', 'Germany', 'Finland', 'Poland', 'Italy',
            'United Kingdom', 'Romania', 'Belarus', 'Kazakhstan', 'Greece', 'Bulgaria', 'Iceland', 'Hungary',
            'Portugal', 'Austria', 'Czech Republic', 'Serbia', 'Ireland', 'Lithuania', 'Latvia', 'Croatia',
            'Bosnia and Herzegovina', 'Slovak Republic', 'Estonia', 'Denmark', 'Switzerland', 'Netherlands', 'Moldova',
            'Belgium', 'Armenia', 'Albania', 'North Macedonia', 'Turkey', 'Slovenia', 'Montenegro', 'Cyprus',
            'Azerbaijan', 'Luxembourg', 'Georgia', 'Andorra', 'Malta', 'Liechtenstein', 'San Marino', 'Monaco']

westernBlock = ['Russian Federation','Poland','Albania','Bulgaria','Romania','Hungary','Czech Republic',
                'Slovak Republic','Bosnia and Herzegovina','Croatia','Montenegro','North Macedonia','Serbia']


eudata = data[data.name.isin(european)]
del data
for i in european:
    eudata.loc[eudata.name == i,'block'] = False
for i in westernBlock:
    eudata.loc[eudata.name == i, 'block'] = True

indicators, counts = np.unique(eudata.indicator,return_counts=True)
index = np.argsort(counts)
# for i in range(len(indicators)):
#     print(indicators[index[i]],counts[index[i]])

interesting = indicators[index[-500:]]
eudata = eudata[eudata.indicator.isin(interesting)]

indicator_timelines = {}
countries_per_indicator = {}

for country in european:
    print(country)
    selection = eudata[eudata.name==country]
    for indicator in np.unique(selection.indicator):
        per_country = selection[selection.indicator==indicator].year
        min = per_country.min()
        max = per_country.max()
        if indicator not in indicator_timelines.keys():
            indicator_timelines[indicator] = []
        indicator_timelines[indicator].append(np.array([min,max]))

for key in indicator_timelines.keys():
    countries_per_indicator[key] = len((indicator_timelines[key]))

for key in indicator_timelines.keys():
    timelines = np.vstack(indicator_timelines[key])
    if np.max(timelines[:,0]) < 1980:
        print("'{}',".format(key))#, "min", np.max(timelines[:,0]),"max",np.min(timelines[:,1]))

indicators = [
'Adjusted savings: education expenditure (% of GNI)',
'Adjusted savings: mineral depletion (current US$)',
'Adolescent fertility rate (births per 1,000 women ages 15-19)',
'Agricultural methane emissions (thousand metric tons of CO2 equivalent)',
'Agricultural nitrous oxide emissions (thousand metric tons of CO2 equivalent)',
'Lower secondary school starting age (years)',
'Methane emissions in energy sector (thousand metric tons of CO2 equivalent)',
'Nitrous oxide emissions in energy sector (thousand metric tons of CO2 equivalent)',
'Other greenhouse gas emissions, HFC, PFC and SF6 (thousand metric tons of CO2 equivalent)',
'Preprimary education, duration (years)',
'Primary education, duration (years)',
'Primary school starting age (years)',
'Secondary education, duration (years)',
'Survival to age 65, female (% of cohort)',
'Survival to age 65, male (% of cohort)',
'Total greenhouse gas emissions (kt of CO2 equivalent)'
]

year_range = list(range(1970,2008))
eudata = eudata[eudata.indicator.isin(indicators)]
eudata = eudata[eudata.year.isin(year_range)]
eudata_byyear = {}

for year in year_range:
    # print(year)
    eudata_byyear[year] = eudata[eudata.year == year]

indicators_Correlation = []
year = 1999
ind = indicators[0]

for year in year_range:
    # print(year)
    euyear = eudata_byyear[year]
    tmp =[]
    for ind in indicators:
        selection = euyear[euyear.indicator == ind]
        corr = np.corrcoef(
            selection.value.values,
            selection.block.values
        )
        # print(corr)
        tmp.append(corr[1,0])

    indicators_Correlation.append(tmp)


corr = np.vstack(indicators_Correlation).T
plt.figure(figsize=(40,10))
plt.imshow(corr)
for i in range(len(corr)):
    for j in range(len(corr[0])):
        plt.text(j,i,'{:.1f}'.format(corr[i,j]),va='center', ha='center')
plt.xticks(np.arange(0,len(year_range),1),year_range,rotation=45)
plt.yticks(np.arange(0,len(indicators),1),indicators,rotation=0,va='center')
plt.show()








