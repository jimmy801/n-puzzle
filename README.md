# N-Puzzle

遊戲介紹
---

[N-Puzzle，數字推盤](https://zh.wikipedia.org/wiki/%E6%95%B8%E5%AD%97%E6%8E%A8%E7%9B%A4%E9%81%8A%E6%88%B2)，拼圖是會以矩形並打亂其順序呈現，拼圖有一個大小相當於一個方塊的空位供方塊移動。遊戲者要移動板上的方塊，讓所有的方塊順著數字的次序排列。

Application Introduction
---

N-Puzzle是使用了`JAVA`所做的一個小遊戲，可以透過滑鼠點擊或是使用鍵盤方向鍵移動拼圖，除了可以載入圖片外，更有透過[A Star Algorithm](https://zh.wikipedia.org/wiki/A*%E7%AE%97%E6%B3%95)來求出最佳解，但由於是NPC問題可能會造成記憶體溢出的可能性，所以如果資料過多，會採用「降階」方式完成

[更多詳細右鍵下載PPT](https://github.com/jimmy801/n-puzzle/blob/master/Read%20me%20PPT/N-Puzzle%20PPT.pptx)

Require
---

Java7以上

遊戲方式
---

1. 起始畫面

  - 會要求輸入長和寬，如果關掉會預設為3X3的棋盤

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/00.png)

2. 操作方式

  - 可以使用滑鼠點擊或鍵盤方向鍵移動自己解題

  - 或使用自動解題(快捷鍵 A)

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/01-1.gif)
  
    ↑3X3

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/01-2.gif)
  
    ↑5X5

3. 可從「檔案」->「開啟」中自訂數字推盤圖片(或使用快捷鍵ctrl+O)

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/02.jpg)

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/03.png)

4. 載入圖片後依然可以使用「自動解題」

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/image%20auto%20solved.gif)

5. 最後結果分為有解即無解

  - 未載入圖片的有無解

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/Solved.png)
    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/No%20Solution.png)

  - 載入圖片的有無解

    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/Image%20Solved.jpg)
    ![image](https://github.com/jimmy801/n-puzzle/blob/master/Screenshot/Image%20No%20Solution.jpg)
