
import sys

import requests
from bs4 import BeautifulSoup


def get_atcoder_data(config, username):
    if not username:
        raise ValueError('Please enter username')

    try:
        response = requests.get(f'https://atcoder.jp/users/{username}')
        # print(response.text)
        html = response.text
        soup = BeautifulSoup(html, 'html.parser')
        rating_span = soup.find('table', class_='dl-table mt-2').find('tr').find_next_sibling().find('td').find('span')
        rating = rating_span.text
        print(rating)

        if response.status_code == 404:
            raise ValueError('The user does not exist')
    except requests.exceptions.RequestException as e:
        raise e



get_atcoder_data(None, sys.argv[1])