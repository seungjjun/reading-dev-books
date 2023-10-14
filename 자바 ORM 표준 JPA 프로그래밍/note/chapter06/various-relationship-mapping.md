# 다양한 연관관게 매핑

<br>

연관관계에는 다음과 같은 다중성이 있다.
- 다대일 (@ManyToOne)
- 일대다 (@OneToMany)
- 일대일 (@OneToOne)
- 다대다 (@ManyToMany)

## 다대일
데이터베이스 테이블의 일(1), 다(N) 관계에서 외래 키는 항상 다쪽에 있다. 따라서 객체 양방향 관계에서 연관관계의 주인은 항상 다쪽이다.  
예를 들어 회원(N)과 팀(1)이 있으면 회원 쪽이 연관관계의 주인이다.

### 다대일 단방향 [N:1]

```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    
    private String username;
}

@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name ="TEAM_ID")
    private Long id;
    
    private String name;
}
```

회원은 Member.team 으로 팀 엔티티를 참조할 수 있지만 반대로 팀에는 회원을 참조하는 필드가 없다. 따라서 회원과 팀은 다대일 단방향 연관관계다.  

### 다대일 양방향 [N:1, 1:N]

```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private String username;
    
    public void setTeam(Team  team) {
        this.team = team;
        
        if(!team.getMembers().contains(this)) {
            team.getMembers().add(this);
        }
    }
}

@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name ="TEAM_ID")
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
    
    public void addMember(Member member) {
        this.members.add(member);
        if (member.getTeam() != this) {
            member.setTeam(team);
        }
    }
}
```
`양방향은 외래 키가 있는 쪽이 연관관계의 주인이다.`
일대다와 다대일 연관관계는 항상 다(N)에 외래 키가 있다.  
MEMBER 테이블이 외래 키를 갖고 있으므로 Member.team이 연관관계의 주인이다.

`양방향 연관관계는 항상 서로를 참조해야 한다.`
양방향 연관관계는 항상 서로 참조해야 하는데 그러려면 연관관계 편의 메서드를 작성하는 것이 좋다.  
위 예시 코드에서 회원의 setTeam(), 팀의 addMember() 메서드가 이런 편의 메서드들이다.  
편의 메서드는 한 곳에만 작성하거나 양쪽 다 작성할 수 있는데, 양쪽에 다 작성하면 무한루프에 빠지므로 주의해야 한다.

## 일대다
일대다 관계는 엔티티를 하나 이상 참조할 수 있으므로 자바 컬렉션인 Collection, List, Set, Map 중에 하나를 사용해야 한다.  

### 일대다 단방향 [1:N]
하나의 팀은 여러 회원을 참조할 수 있는데 이런 관계를 일대다 관계라고 한다.

```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    private String username;
}

@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name ="TEAM_ID")
    private Long id;
    
    private String name;
    
    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>();
}
```

일대다 관계에서는 외래 키는 항상 다쪽 테이블에 있기 때문에 팀 엔티티의 Team.members로 회원 테이블의 TEAM_ID 외래 키를 관리한다.  
다 쪽인 Member 엔티티에 외래 키를 매핑할 수 있는 참조 필드가 없기 때문에 이런 모습이 나타난다.  

일대다 단방향 관계를 매핑할 때는 @JoinCOlumn을 명시해야 한다.

#### 일대다 단방향 매핑의 단점
- 매핑한 객체가 관리하는 외래 키가 다른 테이블에 있다는 점

#### 일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하자
일대다 단방향 매핑을 사용하면 엔티티를 매핑한 테이블이 아닌 다른 테이블의 외래 키를 관리해야 한다는 문제가 있다.  
따라서 일대다 단방향 매핑 대신에 다대일 양방향 매핑을 사용하는 것이 좋다.  다대일 양방향 매핑은 관리해아 하는 외래 키가 본인 테이블에 있기 때문이다.

### 일대다 양방향 [1:N, N:1]
일대다 양방향 매핑은 존재하지 않는다. 대신 다대일 양방향 매핑을 사용해야 한다.  
양방향 매핑에서 @OneToMany는 연관관계의 주인이 될 수 없다. 항상 다 쪽에 외래 키가 있기 때문이다.  
따라서 @OneToMany, @ManyToOne 둘 중에 연관관계의 주인은 항상 다 쪽인 @ManyToOne을 사용한 곳이다. 이런 이유로 @ManyToOne에는 mappedBy 속성이 없다.

```java
@Entity
public class Team {
  @Id @GeneratedValue
  @Column(name ="TEAM_ID")
  private Long id;

  private String name;

  @OneToMany
  @JoinColumn(name = "TEAM_ID")
  private List<Member> members = new ArrayList<>();
}

@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;

    private String username;
}
```

일대다 단방향 매핑 반대편에 다대일 단방향 매핑을 추가해 TEAM_ID 외래 키 컬럼을 매핑했다.  
이때, 둘 다 같은 키를 관리하므로 문제가 발생할 수 있기 때문에 다대일 쪽은 insertable = false, updatable = false 로 설정해 **읽기**만 가능하게 했다.

이는 일대다 단방향 매핑이 가지는 단점을 그대로 가지기 때문에 될 수 있으면 `양방향 매핑`을 사용하자.

## 일대일 [1:1]

일대일 관계는 양쪽이 서로 하나의 관계만 갖는다.  
일대일 관계의 특징
- 일대일 관계는 그 반대도 일대일 관계다.
- 테이블 관계에서 일대다, 다대일은 항상 다(N)쪽이 외래 키를 가진다. 반면에 일대일 관계는 주 테이블이나 대상 테이블 둘 중 어느곳이나 외래 키를 가질 수 있다.

테이블은 주 테이블이든 대상 테이블이든 외래 키 하나만 있으면 양쪽으로 조회할 수 있다. 그래서 누가 외래 키를 가질지 선택해야 한다.

- 주 테이블에 외래 키
  - 주 테이블이 외래 키를 가지고 있으므로 주 테이블만 확인해도 대상 테이블과 연관관계가 있는지 알 수 있다.

- 대상 테이블에 외래 키
  - 테이블 관계를 일대일에서 일대다로 변경할 때 테이블 구조를 그대로 유지할 수 있다.

### 주 테이블에 외래 키
#### 단방향
회원과 사물함의 일대일 단방향 관계
```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;
    
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
}

@Entity
public class Locker {
    @Id @GeneratedValue
    @Column(name ="LOCKER_ID")
    private Long id;

    private String name;
}
```
일대일 관계이므로 @OneToOne을 사용했고 데이터베이스에는 LOCKER_ID 외래 키에 유니크 제약 조건(UNI)을 추가했다. (다대일 단방향(@ManyToOne)과 비슷하다.)

#### 양방향
회원과 사물함의 일대일 양방향 관계
```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;
    
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
}

@Entity
public class Locker {
    @Id @GeneratedValue
    @Column(name ="LOCKER_ID")
    private Long id;

    private String name;
    
    @OneToOne(mappedBy = "locker")
    private Member member;
}
```
양방향이기 때문에 연관관계의 주인을 정해야 한다.  
Member 테이블이 외래 키를 갖고 있으므로 Member 엔티티에 있는 Member.locker가 연관관계의 주인이다.

### 대상 테이블에 외래 키
#### 단방향
일대일 관계 중 대상 테이블에 외래 키가 있는 단방향 관계는 JPA에서 지원하지 않는다.  
이 때는 단방향 관계를 Locker에서 Member 방향으로 수정하거나, 양방향 관계로 만들고 Locker를 연관관계의 주인으로 설정해야 한다.

#### 양방향

```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @OneToOne(mappedBy = "member")
    private Locker locker;
}

@Entity
public class Locker {
    @Id @GeneratedValue
    @Column(name ="LOCKER_ID")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
```

일대일 매핑에서 대상 테이블에 외래 키를 두고 싶으면 위처럼 양방향으로 매핑한다.  
주 엔티티엔 Member 엔티티 대신에 대상 엔티티인 Locker를 연관관계의 주인으로 만들어서 LOCKER 테이블의 외래 키를 관리하도록 했다.

## 다대다 [N:N]
관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다. 그래서 보통 다대다 관계를 일대다, 다대일 관계로 풀어내는 연결 테이블을 사용한다.

그런데 객체는 테이블과 다르게 객체 2개로 다대다 관계를 만들 수 있다.  
예를 들어 회원 객체는 컬렉션을 사용해 상품들을 참조하면 되고 반대로 상품들도 컬렉션을 사용해서 회원들을 참조하면 된다.

### 다대다 연관관계 정리
다대다 관계를 일대다 다대일 관계로 풀어내기 위해 연결 테이블을 만들 때 식별자를 어떻게 구성할지 선택해야 한다.

- 식별 관계: 받아온 식별자를 기본 키 + 외래 키로 사용한다.
- 비식별 관계: 받아온 식별자는 외래 키로만 사용하고 새로운 식별자를 추가한다.

객체 입장에서 보면 비식별 관계를 사용하는 것이 복합 키를 위한 식별자 클래스를 만들지 않아도 되므로 단순하고 편리하게 ORM 매핑을 할 수 있다.
