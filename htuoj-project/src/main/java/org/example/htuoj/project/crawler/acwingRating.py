import http.client
import sys
from bs4 import BeautifulSoup

conn = http.client.HTTPSConnection("www.acwing.com")
payload = ''
headers = {
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'www.acwing.com',
    'Connection': 'keep-alive',

}
conn.request("GET", "/user/myspace/activity/"+ sys.argv[1] +"/1/competition/", payload, headers)
res = conn.getresponse()
data = res.read().decode("utf-8")
soup = BeautifulSoup(data, 'html.parser')
competition_score = soup.find('h4').find('strong').text
print(competition_score)