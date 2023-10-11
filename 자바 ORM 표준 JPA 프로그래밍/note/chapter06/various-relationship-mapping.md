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
