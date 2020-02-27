import requests
from bs4 import BeautifulSoup as bs
import re
import multiprocessing


def getfile(url):
    page = requests.get("https://wolnelektury.pl"+url)
    soup = bs(page.content, 'html.parser')
    if len(re.findall(r'>Language:<', str(soup))) > 0 \
            or len(re.findall(r'Język:<',str(soup)))>0:
        print(url)
        return False

    link= url.split('/')[-1]
    print(link)

    return 'https://wolnelektury.pl/media/book/txt/'+link+'.txt'


URL = 'https://wolnelektury.pl/katalog/'

page = requests.get(URL)
soup = bs(page.content, 'html.parser')

lektury = list(re.finditer(r'\/katalog\/lektura\/[a-zA-Z0-9\-]*', str(soup)))

linki = []
for nazwy in lektury:
    linki.append(str(nazwy.group(0)))
url = linki[0]

result = []

with multiprocessing.Pool(16) as p:
    result = p.map(getfile,linki)


for i in range(len(linki)):
    print(i)
    result.append(getfile(linki[i]))

final =[]
for url in result:
    if url:
        final.append(url+"\n")

with open("booktxt_urls.txt", 'w') as f:
    f.writelines(final)


xargs -a ../booktxt_urls.txt -n 1 -P 500 wget -q

