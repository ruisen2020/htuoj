import re
import sys

import requests
from bs4 import BeautifulSoup

url = "https://codeforces.com/profile/"+sys.argv[1]

payload={}
headers = {
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'codeforces.com',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)


# soup = BeautifulSoup(response.text, 'html.parser')
# rating_span = soup.find_all('span', class_='user-cyan')
# print(rating_span)
pattern = r'unrated.push\(\[(.*?),\s*(.*?),\s*(.*?)\]\);'
matches = re.findall(pattern, response.text)
last_data = matches[-1]
print(last_data[1])