package com.gamificacao.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Email(message = "{Email do Usuário precisa ser valido}")
    @NotEmpty(message = "{Precisa ser cadastrado um email para o Usuário}")
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "{Precisa ser cadastrado o nome completo do Usuário}")
    private String name;
    @NotEmpty(message = "{Precisa ser cadastrado uma para o Usuário}")
    @Length(min = 5, message = "{A senha precisa ter no minimo 5 digitos}")
    private String password;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'images/user.png'")
    private String photo;
    @Column 
    private Integer experience = 0; // atributo para medir o nivel de experiencia total do usuário
    @Column 
    private Integer points = 0; // atributo utilizado para medir quantidade de pontos do usuário para que possa comprar itens
    

	@OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Task> tasksOwned;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public List<Task> getTasksCompleted() {
        return tasksOwned.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksInProgress() {
        return tasksOwned.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }

    public boolean isAdmin() {
        String roleName = "ADMIN";
        return roles.stream().map(Role::getRole).anyMatch(roleName::equals);
    }

    public User() {
    }

    public User(@Email @NotEmpty String email,
                @NotEmpty String name,
                @NotEmpty @Length(min = 5) String password,
                String photo) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.photo = photo;
    }

    public User(@Email @NotEmpty String email,
                @NotEmpty String name,
                @NotEmpty @Length(min = 5) String password,
                String photo,
                List<Task> tasksOwned,
                List<Role> roles) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.photo = photo;
        this.tasksOwned = tasksOwned;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Task> getTasksOwned() {
        return tasksOwned;
    }

    public void setTasksOwned(List<Task> tasksOwned) {
        this.tasksOwned = tasksOwned;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                email.equals(user.email) &&
                name.equals(user.name) &&
                password.equals(user.password) &&
                Objects.equals(photo, user.photo) &&
                Objects.equals(tasksOwned, user.tasksOwned) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password, photo, tasksOwned, roles);
    }

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}
	
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
}
