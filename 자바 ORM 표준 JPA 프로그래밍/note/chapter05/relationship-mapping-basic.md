# 연관관계 매핑 기초

<br>

## 단방향 연관관계

회원과 팀의 관계를 통해 다대일 단방향 관계를 알아보자
![image1](https://blog.kakaocdn.net/dn/oXKs2/btsxtVMbyXZ/eXpkzupho0YBSx2fdVaHNK/img.png)

- 객체 연관관계
  - 회원 객체는 Member.team 필드로 팀 객체와 연관관계를 맺는다.
  - 회원 객체와 팀 객체는 `단방향 관계`다.
    - 회원은 Member.team 필드를 통해 팀을 알 수 있지만 반대로 팀은 회원을 알 수 없다.

- 테이블 연관관계
  - 회원 테이블은 TEAM_ID 외래 키로 팀 테이블과 연관관계를 맺는다.
  - 회원 테이블과 팀 테이븡른 `양방향 관계`다.
    - 회원 테이블의 TEAM_ID 외래 키를 통해서 회원과 팀을 조인할 수 있고 반대로 팀과 회원도 조인할 수 있다.

회원과 팀을 조인하는 SQL
```sql
SELECT *
FROM MEMBER M
JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
```

팀과 회원을 조인하는 SQL
```sql
SELECT *
FROM TEAM T
JOIN MEMBER M ON T.TEAM_ID = M.TEAM_ID
```

### 객체 연관관계와 테이블 연관관계의 가장 큰 차이
참조를 통한 연관관계는 언제나 `단방향`이다.  
객체간에 연관관계를 양방향으로 만들고 싶으면 반대쪽에도 필드를 추가해서 참조를 보관해야 한다.  
`사실은 양방향 관계가 아니라 서로 다른 단방향 관계 2개다.`

양방향 연관관계
```java
class A {
    B b;
}

class B {
    A a;
}
```

### 순수한 객체 연관관계
순수하게 객체만 사용한 회원과 팀의 연관관계 코드
```java
public class Member {
    private String id;
    private String username;
    
    private Team team; // 팀의 참조를 보관
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    // Getter, Setter ...
}

public class Team {
    private String id;
    private String name;
    
    // Getter, Setter ...
}
```

### 테이블 연관관계
데이터베이스 테이블의 회원과 팀의 관계 DDL
```sql
CREATE TABLE MEMBER (
    MEMBER_ID VARCHAR(255) NOT NULL,
    TEAM_ID VARCHAR(255),
    USERNAME VARCHAR(255),
    PRIMARY KEY (MEMBER_ID)
)

CREATE TABLE TEAM (
    TEAM_ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255),
    PRIMARY KEY (TEAM_ID)
)

ALTER TABLE MEMBER ADD CONSTRAINT FK_MEMBER_TEAM
    FOREIGN KEY (TEAM_ID)
    REFERENCES TEAM
```

### 객체 관계 매핑
JPA를 사용해 회원과 팀을 매핑
```java
@Entity
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    
    private String username;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    // Getter, Setter ...
}

@Entity
public class Team {
    @Id
    @Column(name = "TEAM_ID")
    private String id;
    
    private String name;
    
    // Getter, Setter ...
}
```

- @ManyToOne: 다대일(N:1) 관계라는 매핑 정보. 회원과 팀은 다대일 관계
- @JoinColumn: 조인 컬럼은 외래 키를 매핑할 때 사용한다. name 속성에는 매핑할 외래 키 이름을 지정한다.


## 연관관계 사용

### 조회
연관관계가 있는 엔티티를 조회하는 방법은 크게 2가지다.
- 객체 그래프 탐색(객체 연관관계를 사용한 조회)
- 객체지향 쿼리 사용 JPQL

#### 객체 그래프 탐색
```java
Member member = em.find(Member.class, "member1");
Team team = member.getTeam(); // 객체 그래프 탐색
```

객체를 통해 연관된 엔티티를 조회하는 것을 객체 그래프 탐색이라 한다.

#### 객체지향 쿼리 사용
팀1에 소속된 모든 회원을 조회하는 JPQL
```java
private static void queryLogicJoin(EntityManager em) {
    String jpql = "select m from Member m join m.team t where " +
        "t.name=:teamName";
    
    List<Member> resultList = em.createQuery(jpql, Member.class)
        .setParameter("teamName", "팀1")
        .getResultList();
}
```

:teamName 과 같이 :로 시작하는 것은 파리미터를 바인딩받는 문법이다.

### 연관된 엔티티 삭제
연관된 엔티티를 삭제하려면 기존에 있던 연관관계를 먼저 제거하고 삭제해야 한다.  
예를 들어 팀1에 회원1과 회원2가 소속되어 있을 때, 팀1을 삭제하려면 연관관계(회원1, 회원2)를 먼저 끊어야 한다.

## 양방향 연관관계
![image2](https://blog.kakaocdn.net/dn/UQ5RE/btsxqsqHoDi/4PVvBfek5o9ki3LI1GGoK1/img.png)

회원과 팀은 다대일 관계이고 반대로 팀에서 회원은 일대다 관계다.  
일대다 관계는 여러 건과 연관관계를 맺을 수 있으므로 컬렉션을 사용해야 한다. (Team.members를 List 컬렉션으로 추가)

- 회원 -> 팀 (Member.team)
- 팀 -> 회원 (Team.members)

```java
@Entity
public class Member {
  @Id
  @Column(name = "MEMBER_ID")
  private String id;

  private String username;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
  private Team team;

  public void setTeam(Team team) {
    this.team = team;
  }

  // Getter, Setter ...
}

@Entity
public class Team {
    @Id
    @Column(name = "TEAM_ID")
    private String id;
    
    private String name;
    
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
    
    // Getter, Setter ...
}
```

일대다 관계를 매핑하기 위해 @OneToMany 매핑 정보를 사용했다.  
mappedBy 속성은 양방향 매핑일 때 사용하는데 반때쪽 매핑의 필드 이름을 값으로 주면 된다.  
반대쪽 매핑이 Member.team이므로 team을 값으로 주었다.

## 연관관계의 주인
엄밀히 이야기하면 객체에는 양방향 관계라는 것이 없다. 서로 다른 단방향 연관관계 2개를 애플리케이션 로직으로 잘 묶어서 양방향인 것처럼 보이게 할 뿐이다.  

두 객체 연관관계 중 하나를 정해서 테이블의 외래키를 관리해야 하는데 이것을 `연관관계의 주인`이라 한다.

### 양방향 매핑의 규칙: 연관관계의 주인
양방향 연관관계 매핑 시 지켜야 할 규칙: 두 연관관계 중 하나를 연관관계의 주인으로 정해야 한다.

연관관계의 주인만이 데이터베이스 연관관계와 매핑되고 외래 키를 관리(등록, 수정, 삭제)할 수 있다. 반면에 주인이 아닌 쪽은 읽기만 할 수 있다.

어떤 연관관계르 주인으로 정할지는 mappedBy 속성을 사용
- 주인은 mappedBy 속성을 사용하지 않는다.
- 주인이 아니면 mappedBy 속성을 사용해서 속성의 값으로 연관관계의 주인을 지정해야 한다.

연관관계의 주인을 정한다는 것은 사실 외래 키 관리자를 선택하는 것이다.

### 연관관계의 주인은 외래 키가 있는 곳
연관관계의 주인은 테이블에 외래 키가 있는 곳으로 정해야 한다. 여기서는 회원 테이블이 외래 키를 가지고 있으므로 Member.team이 주인이 된다.  
주인이 아닌 Team.members에는 mappedBy="team" 속성을 사용해서 주인이 아님을 설정한다.

```html
데이터베이스 테이블의 다대일, 일대다 관계에서는 항상 다 쪽이 외래 키를 가진다. 다 쪽인 @ManyToOne은 항상 연관관계의 주인이 되므로 mappedBy를 설정할 수 없다.
따라서 @ManyToOne에는 mappedBy 속성이 없다.
```
