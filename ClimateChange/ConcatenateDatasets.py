import pandas as pd
import pathlib

path = pathlib.Path("data")
climateChange = list(path.glob('*climate-change*'))
indicators = list(path.glob("*indicators*"))

CountryWDI = pd.read_csv('data/WDICountry.csv')
DataWDI = pd.read_csv('data/WDIData.csv')

climate = [pd.read_csv(file, index_col=None, header=1) for file in climateChange]
indicator = [pd.read_csv(file, index_col=None, header=1) for file in indicators]
final = pd.concat(climate + indicator, ignore_index=True)
final = final.rename(
    columns={'#country+name': 'name', '#country+code': 'code', '#date+year': 'year', '#indicator+name': 'indicator',
             '#indicator+code': 'indcode', '#indicator+value+num': 'value'})
del climateChange
del indicators
del climate
del indicator

DataProcessed = []
for year in range(1960, 2020):
    selection = DataWDI[DataWDI[str(year)].notna()].get(
        ["Country Name", "Country Code", "Indicator Name", "Indicator Code", str(year)])
    selection["year"] = year
    selection = selection.rename(columns={"Country Name": 'name', "Country Code": 'code', "Indicator Name": 'indicator',
                                          'Indicator Code': 'indcode', str(year): 'value'})
    DataProcessed.append(selection)

final = pd.concat([final] + DataProcessed)
del DataWDI
del DataProcessed

final.to_csv("Final_Dataset.csv")