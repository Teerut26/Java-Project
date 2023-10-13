# EventHub

## การติดตั้งโปรแกรม

### MacOS Install (Chip Apple Silicon)

[Download](./release/EventHub-AppleM1.zip)

### Windows Install

[Download](./release/EventHub-Windows.zip)

### เมื่อ Download เสร็จแล้ว
- แตกไฟล์และเข้าไปใน folder ที่แตก
- เปิดผ่าน Command Line เลือกใช้คำสั่งตามระบบปฏิบัติการ
    - ใช้คำสั่ง `java -jar EventHub-AppleM1.jar` 
    - ใช้คำสั่ง `java -jar EventHub-Windows.jar` 

---

## Documentation
[Documentation](./docs/manual.pdf)

## ตัวอย่างข้อมูลผู้ใช้ในระบบ (username,password)

| username     | password     | role    |
| :----------- | :----------- | :------ |
| admin        | admin        | `admin` |
| beer         | 1234         | `user`  |
| noey         | 1234         | `user`  |
| flim         | 1234         | `user`  |
| cynteer      | 1234         | `user`  |
| hope         | hope         | `user`  |
| john         | john         | `user`  |
| alice        | alice        | `user`  |
| susan        | susan        | `user`  |
| johndoe      | johndoe      | `user`  |
| ilovecoding  | ilovecoding  | `user`  |
| thegamer     | thegamer     | `user`  |
| musiclover   | musiclover   | `user`  |
| techgeek     | techgeek     | `user`  |
| bookworm     | bookworm     | `user`  |
| happyface    | happyface    | `user`  |
| sunshine     | sunshine     | `user`  |
| naturelover  | naturelover  | `user`  |
| coffeeaddict | coffeeaddict | `user`  |
| foodie       | foodie       | `user`  |
| sandy        | sandy        | `user`  |
| randy        | randy        | `user`  |
| velma        | velma        | `user`  |
| jayden       | jayden       | `user`  |
| julie        | julie        | `user`  |
| jemma        | jemma        | `user`  |
| art          | art          | `user`  |
| donn         | donn         | `user`  |
| james        | james        | `user`  |
| kelly        | kelly        | `user`  |
| linda        | linda        | `user`  |
| fred         | fred         | `user`  |
| kuwin        | kuwin        | `user`  |

## Folder Structure

```
.
├── README.md
├── data
│   ├── csv
│   │   ├── activitiesEvent.csv
│   │   ├── activitiesTeam.csv
│   │   ├── commentsActivitiesEvent.csv
│   │   ├── commentsActivitiesTeam.csv
│   │   ├── events.csv
│   │   ├── mtm
│   │   │   ├── _userToEvent.csv
│   │   │   ├── _userToEventSuspend.csv
│   │   │   ├── _userToTeam.csv
│   │   │   ├── _userToTeamExtra.csv
│   │   │   ├── _userToTeamHead.csv
│   │   │   ├── _userToTeamSuspend.csv
│   │   ├── team.csv
│   │   └── users.csv
│   └── images
│           ├── event (เก็บ event image)
│           └── user (เก็บ user profile image)
├── dependency-reduced-pom.xml
├── docs
│   └── manual.pdf
├── image
│   └── Screenshot 2566-10-11 at 12.52.17.png
├── mvnw
├── mvnw.cmd
├── pom.xml
├── release
│   ├── EventHub-AppleM1.jar
│   ├── EventHub-AppleM1.zip
│   ├── EventHub-Windows.jar
│   ├── EventHub-Windows.zip
│   └── data
│       ├── csv
│       │   ├── activitiesEvent.csv
│       │   ├── activitiesTeam.csv
│       │   ├── commentsActivitiesEvent.csv
│       │   ├── commentsActivitiesTeam.csv
│       │   ├── events.csv
│       │   ├── mtm
│       │   │   ├── _userToEvent.csv
│       │   │   ├── _userToEventSuspend.csv
│       │   │   ├── _userToTeam.csv
│       │   │   ├── _userToTeamExtra.csv
│       │   │   ├── _userToTeamHead.csv
│       │   │   └── _userToTeamSuspend.csv
│       │   ├── team.csv
│       │   └── users.csv
│       └── images
│           ├── event (เก็บ event image)
│           └── user (เก็บ user profile image)
└── src
    ├── main
    │   ├── java
    │   │   ├── cs211
    │   │   │   └── project
    │   │   │       ├── Main.java
    │   │   │       ├── controllers
    │   │   │       │   ├── AboutUs.java
    │   │   │       │   ├── HelloController.java
    │   │   │       │   ├── admin
    │   │   │       │   │   └── ManageUserController.java
    │   │   │       │   ├── auth
    │   │   │       │   │   ├── LoginPageController.java
    │   │   │       │   │   └── RegisterPageController.java
    │   │   │       │   ├── components
    │   │   │       │   │   ├── CommentCardComponentController.java
    │   │   │       │   │   ├── EventCardComponentController.java
    │   │   │       │   │   ├── InProcessCardComponentController.java
    │   │   │       │   │   ├── InTeamCardComponentController.java
    │   │   │       │   │   ├── NavBarComponentController.java
    │   │   │       │   │   ├── SideBarComponentController.java
    │   │   │       │   │   └── admin
    │   │   │       │   │       ├── AdminNavbarComponentController.java
    │   │   │       │   │       └── AdminSideBarComponentController.java
    │   │   │       │   ├── event
    │   │   │       │   │   ├── CommentActivityEventController.java
    │   │   │       │   │   ├── EventDetailController.java
    │   │   │       │   │   ├── EventListController.java
    │   │   │       │   │   ├── JoinEventController.java
    │   │   │       │   │   └── team
    │   │   │       │   │       ├── CommentActivityTeamController.java
    │   │   │       │   │       ├── EventTeamDetailController.java
    │   │   │       │   │       └── EventTeamListController.java
    │   │   │       │   ├── eventHistory
    │   │   │       │   │   └── InProcessAndEndedController.java
    │   │   │       │   ├── myEvent
    │   │   │       │   │   ├── CreateEventDetailFormController.java
    │   │   │       │   │   ├── CreateTeamController.java
    │   │   │       │   │   ├── EditEventDetailFormController.java
    │   │   │       │   │   ├── EditTeam.java
    │   │   │       │   │   ├── MyEventViewController.java
    │   │   │       │   │   └── SetEventDetailController.java
    │   │   │       │   ├── myTeam
    │   │   │       │   │   ├── MyTeamController.java
    │   │   │       │   │   └── ViewMyTeamController.java
    │   │   │       │   ├── profile
    │   │   │       │   │   ├── ChangePasswordProfileController.java
    │   │   │       │   │   └── MyProfilePageController.java
    │   │   │       │   └── schedule
    │   │   │       │       ├── AddScheduleParticipantController.java
    │   │   │       │       ├── AddScheduleTeamController.java
    │   │   │       │       ├── EditScheduleParticipantController.java
    │   │   │       │       └── EditScheduleTeamController.java
    │   │   │       ├── models
    │   │   │       │   ├── Activities.java
    │   │   │       │   ├── ActivitiesEvent.java
    │   │   │       │   ├── ActivitiesTeam.java
    │   │   │       │   ├── Comment.java
    │   │   │       │   ├── CommentActivitiesEvent.java
    │   │   │       │   ├── CommentActivitiesTeam.java
    │   │   │       │   ├── Event.java
    │   │   │       │   ├── ManyToMany.java
    │   │   │       │   ├── Team.java
    │   │   │       │   ├── User.java
    │   │   │       │   └── collections
    │   │   │       │       ├── ActivitiesCollection.java
    │   │   │       │       ├── ActivitiesEventCollection.java
    │   │   │       │       ├── ActivitiesTeamCollection.java
    │   │   │       │       ├── CommentActivitiesEventCollection.java
    │   │   │       │       ├── CommentActivitiesTeamCollection.java
    │   │   │       │       ├── CommentCollection.java
    │   │   │       │       ├── EventCollection.java
    │   │   │       │       ├── ManyToManyCollection.java
    │   │   │       │       ├── TeamCollection.java
    │   │   │       │       └── UserCollection.java
    │   │   │       ├── router
    │   │   │       │   └── Router.java
    │   │   │       ├── services
    │   │   │       │   ├── DatasourceInterface.java
    │   │   │       │   ├── FXRouter.java
    │   │   │       │   ├── ManyToManyManager.java
    │   │   │       │   ├── RouteProvider.java
    │   │   │       │   ├── SingletonStorage.java
    │   │   │       │   ├── comparator
    │   │   │       │   │   └── UserLastloginComparator.java
    │   │   │       │   ├── datasource
    │   │   │       │   │   ├── ActivitiesEventFileListDatesource.java
    │   │   │       │   │   ├── ActivitiesTeamFileListDatesource.java
    │   │   │       │   │   ├── CommentActivitiesEventFileListDatasource.java
    │   │   │       │   │   ├── CommentActivitiesTeamFileListDatasource.java
    │   │   │       │   │   ├── EventFileListDatesource.java
    │   │   │       │   │   ├── ManyToManyFileListDatasource.java
    │   │   │       │   │   ├── TeamFileListDatasource.java
    │   │   │       │   │   └── UserFileListDatasource.java
    │   │   │       │   └── deleterelated
    │   │   │       │       ├── DeleteRelatedOfPrimaryKeyEvent.java
    │   │   │       │       └── DeleteRelatedOfPrimaryKeyTeam.java
    │   │   │       └── utils
    │   │   │           ├── CSVManager.java
    │   │   │           ├── ComponentLoader.java
    │   │   │           ├── ComponentRegister.java
    │   │   │           ├── FileIO.java
    │   │   │           ├── ImageSaver.java
    │   │   │           ├── NewLineClear.java
    │   │   │           ├── ReplaceComma.java
    │   │   │           └── TimeValidate.java
    │   │   └── module-info.java
    │   └── resources
    │       └── cs211
    │           └── project
    │               ├── assets
    │               │   ├── about-us
    │               │   │   ├── beer.jpg
    │               │   │   ├── hope.jpg
    │               │   │   ├── jar.jpg
    │               │   │   └── ppp.png
    │               │   ├── font
    │               │   ├── image
    │               │   │   ├── auth
    │               │   │   │   ├── calender.png
    │               │   │   │   ├── community.png
    │               │   │   │   └── user.png
    │               │   │   └── icons
    │               │   │       ├── 6714978.png
    │               │   │       ├── box.png
    │               │   │       ├── file.png
    │               │   │       ├── free-light-mode-4562903-3856596.png
    │               │   │       ├── gantt-chart-square.png
    │               │   │       └── users.png
    │               │   └── manual.pdf
    │               ├── components
    │               │   ├── CommentCardComponent.fxml
    │               │   ├── EndedEventCardComponent.fxml
    │               │   ├── EventCardComponent.fxml
    │               │   ├── InProcessEventCardComponent.fxml
    │               │   ├── InTeamEventCardComponent.fxml
    │               │   ├── NavBarComponent.fxml
    │               │   ├── SideBarComponent.fxml
    │               │   └── admin
    │               │       ├── AdminNavbarComponent.fxml
    │               │       └── AdminSideBarComponent.fxml
    │               ├── style
    │               │   ├── all.css
    │               │   ├── dark-mode.css
    │               │   ├── font-style1.css
    │               │   ├── font-style2.css
    │               │   ├── font-style3.css
    │               │   └── light-mode.css
    │               ├── template
    │               │   └── event-joined-detail.fxml
    │               └── views
    │                   ├── Schedule
    │                   │   ├── add-schedule-participant.fxml
    │                   │   ├── add-schedule-team.fxml
    │                   │   ├── edit-schedule-participant.fxml
    │                   │   └── edit-schedule-team.fxml
    │                   ├── about-us.fxml
    │                   ├── admin
    │                   │   └── manage-user.fxml
    │                   ├── auth
    │                   │   ├── login-page.fxml
    │                   │   └── register-page.fxml
    │                   ├── event
    │                   │   ├── comment-activity-event.fxml
    │                   │   ├── event-detail.fxml
    │                   │   ├── event-list.fxml
    │                   │   ├── join-event.fxml
    │                   │   └── team
    │                   │       ├── comment-activity-team.fxml
    │                   │       ├── event-team-activities-list.fxml
    │                   │       ├── event-team-detail.fxml
    │                   │       ├── event-team-list.fxml
    │                   │       └── event-team-member.fxml
    │                   ├── eventHistory
    │                   │   └── in-process-and-ended.fxml
    │                   ├── hello-view.fxml
    │                   ├── myEvent
    │                   │   ├── create-event-detail-form.fxml
    │                   │   ├── create-team.fxml
    │                   │   ├── edit-event-detail-form.fxml
    │                   │   ├── edit-participant.fxml
    │                   │   ├── edit-team.fxml
    │                   │   ├── my-event-view.fxml
    │                   │   └── set-event-detail.fxml
    │                   ├── myTeam
    │                   │   ├── my-team.fxml
    │                   │   └── view-my-team.fxml
    │                   └── profile
    │                       ├── change-password-profile-page.fxml
    │                       └── my-profile-page.fxml
    └── test
        └── java
            └── cs211
                └── project
                    ├── models
                    │   └── collections
                    │       └── ManyToManyCollectionTest.java
                    ├── services
                    │   └── ManyToManyManagerTest.java
                    └── utils
```

## สมาชิกผู้จัดทำ

-   ธีรุฒ ศรีทองดี `6510405601` [@Teerut26](https://www.github.com/Teerut26) `หมู่เรียนที่ 12`
-   กรรวี ศิริขันธ์ `6510405270` [@pwknnn](https://www.github.com/pwknnn) `หมู่เรียนที่ 12`
-   ศราวุธ อินพล `6510405822` [@beerth21624](https://www.github.com/beerth21624) `หมู่เรียนที่ 11`
-   เบญจพร สุขไพบูลย์ `6510405636` [@JarBenjaporn](https://www.github.com/JarBenjaporn) `หมู่เรียนที่ 12`
