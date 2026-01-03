import re
import ssl
import urllib.request, urllib.error
import bs4
import sys
baseUrl = "https://www.luogu.com.cn/problem/"
# 修改此处配置
savePath = "D:\\htuoj\\htuoj-project\\src\\main\\resources\\problem.md"  # 保存路径
maxn = sys.argv[1]


def main():
    print("计划爬取到{}".format(maxn))

    print("正在爬取{}...".format(maxn), end="")
    html = getHTML(baseUrl + str(maxn))
    if html == "error":
        print("爬取失败，可能是不存在该题或无权查看")
    else:
        problemMD = getMD(html)
        print("爬取成功！正在保存...", end="")
        saveData(problemMD, "problem" + ".md")
        print("保存成功!")
    print("爬取完毕")


def getHTML(url):
    headers = {
        "user-agent": "Mozilla / 5.0(Windows NT 10.0;Win64;x64) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 85.0.4183.121 Safari / 537.36"
    }
    request = urllib.request.Request(url=url, headers=headers)
    context = ssl._create_unverified_context()
    response = urllib.request.urlopen(request, context=context)
    html = response.read().decode('utf-8')
    if str(html).find("Exception") == -1:  # 洛谷中没找到该题目或无权查看的提示网页中会有该字样
        return html
    else:
        return "error"


def getMD(html):
    bs = bs4.BeautifulSoup(html, "html.parser")
    core = bs.select("article")[0]
    md = str(core)
    md = re.sub("<h1>", "# ", md)
    md = re.sub("<h2>", "## ", md)
    md = re.sub("<h3>", "## ", md)
    md = re.sub("</?[a-zA-Z]+[^<>]*>", "", md)
    return md


def saveData(data, filename):
    cfilename = savePath
    file = open(cfilename, "w", encoding="utf-8")
    for d in data:
        file.writelines(d)
    file.close()


if __name__ == '__main__':
    main()
