import requests
import sys
from bs4 import BeautifulSoup

url = "https://ac.nowcoder.com/acm/contest/profile/"+sys.argv[1]

payload={}
headers = {
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'ac.nowcoder.com',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

soup = BeautifulSoup(response.text, 'html.parser')
rating = soup.find('div', class_='state-num rate-score3').text
print(rating)