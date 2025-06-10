package com.blacksabbath.lumitunespring.model;

import java.util.List;
import java.util.UUID;

import com.blacksabbath.lumitunespring.misc.Roles;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(unique = true, length = 255, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Image> images;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "avatar_id")
	private Image avatar;

	@Column(nullable = true)
	private Roles role;

	@Column(nullable = false)
	private int accSubscribers;

	@Column(nullable = false)
	private int accFollowings;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "data_id", referencedColumnName = "id")
	@JsonManagedReference
	private UserData userData;

	public User() {
	}

	public User(String username, String password, List<Image> images, UserData userData, Roles role, int accSubscribers,
			int accFollowings, Image avatar) {
		this.username = username;
		this.password = password;
		this.images = images;
		this.userData = userData;
		this.role = role;
		this.accSubscribers = accSubscribers;
		this.accFollowings = accFollowings;
		this.avatar = avatar;
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Image> getImages() {
		return images;
	}

	public UserData getUserData() {
		return userData;
	}

	public Roles getRole() {
		return this.role;
	}

	public int getAccSubscribers() {
		return accSubscribers;
	}

	public int getAccFollowings() {
		return accFollowings;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public void setRoleId(Roles role) {
		this.role = role;
	}

	public void setAccSubscribers(int accSubscribers) {
		this.accSubscribers = accSubscribers;
	}

	public void setAccFollowings(int accFollowings) {
		this.accFollowings = accFollowings;
	}

	public Image getAvatar() {
		return this.avatar;
	}

	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}

}
