#ifndef _INDICATOR_H_
#define _INDICATOR_H_

static short numIndicatorIndices = 36;

static const float indicatorVertices[] =
{
-5.0f, 0.0f, 5.0f,
-5.0f, 0.0f, -5.0f,
5.0f, 0.0f, -5.0f,
5.0f, 0.0f, 5.0f,
5.0f, 0.0f, 5.0f,
5.0f, 0.0f, -5.0f,
-5.0f, 0.0f, -5.0f,
-5.0f, 0.0f, 5.0f,
0.0f, -5.0f, 5.0f,
0.0f, -5.0f, -5.0f,
0.0f, 5.0f, -5.0f,
0.0f, 5.0f, 5.0f,
0.0f, 5.0f, 5.0f,
0.0f, 5.0f, -5.0f,
0.0f, -5.0f, -5.0f,
0.0f, -5.0f, 5.0f,
-5.0f, 5.0f, 0.0f,
-5.0f, -5.0f, 0.0f,
5.0f, -5.0f, 0.0f,
5.0f, 5.0f, 0.0f,
5.0f, 5.0f, 0.0f,
5.0f, -5.0f, 0.0f,
-5.0f, -5.0f, 0.0f,
-5.0f, 5.0f, 0.0f
};

static const float indicatorNormals[] =
{
0.0f, -1.0f, 0.0f,
0.0f, -1.0f, 0.0f,
0.0f, -1.0f, 0.0f,
0.0f, -1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
-1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f
};

static const float indicatorTexcoords[] =
{
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
1.0f, 1.0f, 1.0f,
1.0f, 1.0f, 0.0f,
0.0f, 0.0f, 0.0f,
1.0f, 0.0f, 1.0f,
0.0f, 0.0f, 1.0f,
0.0f, 1.0f, 1.0f,
1.0f, 1.0f, 1.0f,
0.0f, 0.0f, 0.0f,
0.0f, 1.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 1.0f,
1.0f, 1.0f, 1.0f,
1.0f, 0.0f, 0.0f,
0.0f, 0.0f, 1.0f
};

static const unsigned short indicatorIndices[] =
{
0, 1, 2,
2, 3, 0,
4, 5, 6,
6, 7, 4,
8, 9, 10,
10, 11, 8,
12, 13, 14,
14, 15, 12,
16, 17, 18,
18, 19, 16,
20, 21, 22,
22, 23, 20
};

#endif // _INDICATOR_H_
