# API Endpoints

## Web Endpoints (Thymeleaf)

| Endpoint                                      | Method | Description            |
| --------------------------------------------- | ------ | ---------------------- |
| `/posts`                                      | GET    | View all posts         |
| `/posts/new`                                  | GET    | Show create post form  |
| `/posts`                                      | POST   | Create a new post      |
| `/posts/{id}`                                 | GET    | View post details      |
| `/posts/edit/{id}`                            | GET    | Show edit post form    |
| `/posts/edit/{id}`                            | POST   | Update a post          |
| `/posts/delete/{id}`                          | POST   | Delete a post          |
| `/posts/{id}/comments`                        | POST   | Add a comment          |
| `/posts/{postId}/comments/edit/{commentId}`   | GET    | Show edit comment form |
| `/posts/{postId}/comments/edit/{commentId}`   | POST   | Update a comment       |
| `/posts/{postId}/comments/delete/{commentId}` | POST   | Delete a comment       |
| `/register`                                   | GET    | Show registration page |
| `/register`                                   | POST   | Register a new user    |
| `/login`                                      | GET    | Show login page        |
| `/login`                                      | POST   | Authenticate user      |
| `/logout`                                     | POST   | Logout user            |

---

## REST API Endpoints

### Posts API

| Endpoint          | Method | Description              |
| ----------------- | ------ | ------------------------ |
| `/api/posts`      | GET    | Retrieve paginated posts |
| `/api/posts/{id}` | GET    | Retrieve post by ID      |
| `/api/posts`      | POST   | Create a post            |
| `/api/posts/{id}` | PUT    | Update a post            |
| `/api/posts/{id}` | DELETE | Delete a post            |

### Comments API

| Endpoint                          | Method | Description                  |
| --------------------------------- | ------ | ---------------------------- |
| `/api/posts/{postId}/comments`    | GET    | Retrieve comments for a post |
| `/api/posts/{postId}/comments`    | POST   | Create a comment             |
| `/api/posts/comments/{commentId}` | PUT    | Update a comment             |
| `/api/posts/comments/{commentId}` | DELETE | Delete a comment             |

---

