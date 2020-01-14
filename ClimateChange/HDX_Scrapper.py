import requests
from bs4 import BeautifulSoup as bs
import re
import multiprocessing

def getDownloadLink(url):
    page = requests.get("https://data.humdata.org" + url)
    return "https://data.humdata.org" + re.search(r'\/[0-9a-z\/_%-]*.csv', str(page)).group(0) + "\n"


def get_url(link):
    return re.search(r'\/dataset[a-z\/-]*',str(link)).group(0)


URL = 'https://data.humdata.org/search?res_format=CSV&organization=hdx&q=climate%20change\&ext_page_size=500&sort=title_case_insensitive%20asc'
# URL = 'https://data.humdata.org/search?res_format=CSV&organization=hdx&q=climate%20change\&ext_page_size=25&sort=title_case_insensitive%20asc'


page = requests.get(URL)
soup = bs(page.content, 'html.parser')
data_pages = list(map(get_url ,soup.find_all('div', class_='dataset-heading')))
print('found ',len(data_pages),"datasets")

with multiprocessing.Pool() as p:
    results = p.map(getDownloadLink, data_pages)
print("Saving to file")
with open("dataset_urls.txt", 'w') as f:
    f.writelines(results)