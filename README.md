# Dash Divider
Dashed divider for RecyclerView

## Installation

**Gradle:**
```groovy
compile 'com.zac4j.decor:dashdivider:0.0.1'
```

**Maven:**
```
<dependency>
  <groupId>com.zac4j.decor</groupId>
  <artifactId>dashdivider</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```

## How do I use it?

The usage is really simple.

**First set RecyclerView `android:layerType="software"` attribute: **
```xml
<android.support.v7.widget.RecyclerView
    android:id="@id/main_rv_list"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layerType="software"
    android:padding="@dimen/space.small"
    tools:listitem="@layout/list_item_main"
    />
```

**Then, If your RecyclerView hold a LinearLayoutManager, Use DashDivider:**
```
  private void updateUi() {
    ...
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mCtx);
    mRecyclerView.setLayoutManager(mLayoutManager);
    RecyclerView.ItemDecoration dashDivider = new DashDivider.Builder(mCtx)
        .dashGap(5)
        .dashLength(5)
        .dashThickness(3)
        .color(ContextCompat.getColor(this, R.color.colorPrimary))
        .orientation(mLayoutManager.getOrientation())
        .build();
    mRecyclerView.addItemDecoration(dashDivider);
    ...
  }
```

**Else if your RecyclerView hold a GridLayoutManager, User GridDashDivider:**
```
    private static final int DEFAULT_SPAN_COUNT = 4;
    ...
    mGridRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, DEFAULT_SPAN_COUNT));
    RecyclerView.ItemDecoration gridDivider = new GridDashDivider.Builder(mCtx)
        .dashGap(5)
        .dashLength(5)
        .dashThickness(3)
        .color(ContextCompat.getColor(this, R.color.colorPrimary))
        .drawer(true, false, true, false)
        .offset(10, 10, 10, 10)
        .build();
    mGridRecyclerView.addItemDecoration(gridDivider);
```

**You could check sample project usage:**
[Sample Project][sample]

## Contributions

Feel free to create issues / pull requests.

## License

```
Copyright (c) 2017 Zaccc (http://github.com/zac4j).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[sample]:https://github.com/zac4j/dashdivider/blob/master/app/src/main/java/com/zac4j/sample/MainActivity.java
