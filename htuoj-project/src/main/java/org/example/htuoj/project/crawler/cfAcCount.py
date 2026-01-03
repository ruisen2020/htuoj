import requests
import sys
from bs4 import BeautifulSoup

headers = {
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Content-Type': 'application/json'
}

response = requests.get('https://codeforces.com/profile/' + sys.argv[1], headers=headers)

soup = BeautifulSoup(response.text, 'html.parser')

counters_rows = soup.find('div', class_='_UserActivityFrame_footer').find_all('div',
                                                                              class_='_UserActivityFrame_countersRow')

results = []
for row in counters_rows:
    counters = row.find_all('div', class_='_UserActivityFrame_counter')
    for counter in counters:
        value = counter.find('div', class_='_UserActivityFrame_counterValue').text
        description = counter.find('div', class_='_UserActivityFrame_counterDescription').text
        results.append((value, description))

for num, desc in results:
    print(num.split(' ')[0])
    break
