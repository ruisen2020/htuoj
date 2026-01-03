import requests
import sys
from bs4 import BeautifulSoup

# headers = {
#     'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
#     'Content-Type': 'application/json'
# }
response = requests.get('https://ac.nowcoder.com/acm/contest/profile/'+sys.argv[1]+'/practice-coding')
soup = BeautifulSoup(response.text, 'html.parser')
state_items = soup.find('div', class_='my-state-main').find_all('div', class_='my-state-item')

results = []
for item in state_items:
    state_num = item.find('div', class_='state-num').text
    description = item.find('span').text
    results.append((state_num, description))

for num, desc in results[1:]:
    print(num)
    break
