# hashtag
Hashtag generator

### create new tags
```bash
java -jar hashtag.jar new sky city
new: 14
#streetcorner #alleylight #cityview #sunrise #sky #city #clearsky
#urbanlandscape #citylife #bluesky #skyview #buildings #sunset
#sidewalkscene
```
this will create a new file `hashtag.txt` with tags related to `sky` and `city`

### edit tags
```bash
vi hashtag.txt
```

### copy tags from another instagram post
```bash
java -jar hashtag.jar copy
copy: 15
#streetcorner #alleylight #cityview #sunrise #sky #city #clearsky
#urbanlandscape #citylife #bluesky #skyview #buildings #sunset
#sidewalkscene #TimHortons
```
this will connect to the mobile phone by adb and copy the tags currently on the screen to `hashtag.txt`

### shuffle tags
```bash
java -jar hashtag.jar shuffle
shuffle: 15
#sky #skyview #sunset #citylife #clearsky #buildings #city #cityview
#sunrise #sidewalkscene #bluesky #streetcorner #alleylight #TimHortons
#urbanlandscape
```

### paste tags to instagram post
```bash
java -jar hashtag.jar paste
paste: 15
#sky #skyview #sunset #citylife #clearsky #buildings #city #cityview
#sunrise #sidewalkscene #bluesky #streetcorner #alleylight #TimHortons
#urbanlandscape
```
this will connect to the mobile phone by adb and paste the tags to the current field
