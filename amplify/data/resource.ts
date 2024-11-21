import { type ClientSchema, a, defineData } from "@aws-amplify/backend";

/*== STEP 1 ===============================================================
The section below creates a Todo database table with a "content" field. Try
adding a new "isDone" field as a boolean. The authorization rule below
specifies that any user authenticated via an API key can "create", "read",
"update", and "delete" any "Todo" records.
=========================================================================*/
const schema = a.schema({
  Wishlist: a.model({
    id: a.id().required(),
    userId: a.string().required(),
    user: a.belongsTo("User", "userId"),
    books: a.hasMany("BookWishlist", "wishlistId")
  }),

  BookWishlist: a.model({
    bookId: a.id(),
    wishlistId: a.id().required(),
    book: a.belongsTo("Book", "bookId"),
    list: a.belongsTo("Wishlist", "wishlistId")
  }),

  Book: a.model({
    id: a.id().required(),
    title: a.string().required(),
    isbn: a.string().required(),
    authorId: a.string().required(),
    author: a.belongsTo("Author", "authorId"),
    categories: a.hasMany("BookCategory", "bookId"),
    ratings: a.hasMany("BookRating", "bookId"),
    listings: a.hasMany("Listing", "bookId"),
    wishlists: a.hasMany("BookWishlist", "bookId"),
    libraries: a.hasMany("BookLibrary", "bookId")
  }).secondaryIndexes((index) => [index("isbn")]),

  UserLibrary: a.model({
    id: a.id().required(),
    userId: a.string().required(),
    user: a.belongsTo("User", "userId"),
    books: a.hasMany("BookLibrary", "libraryId"),
  }),

  BookLibrary: a.model({
    bookId: a.id(),
    libraryId: a.id().required(),
    book: a.belongsTo("Book", "bookId"),
    library: a.belongsTo("UserLibrary", "libraryId")
  }),

  BookCategory: a.model({
    categoryId: a.string(),
    bookId: a.string().required(),
    category: a.belongsTo("Category", "categoryId"),
    book: a.belongsTo("Book", "bookId"),
  }),

  Author: a.model({
    id: a.id().required(),
    name: a.string().required(),
    books: a.hasMany("Book", "authorId"),
  }),

  User: a.model({
    email: a.string().required(),
    firstName: a.string(),
    lastName: a.string(),
    address: a.string(),
    phone: a.string(),
    library: a.hasOne("UserLibrary", "userId"),
    ratingsReceived: a.hasMany("UserRating", "ratedId"),
    ratings: a.hasMany("UserRating", "userId"),
    listings: a.hasMany("Listing", "userId"),
    wishlist: a.hasOne("Wishlist", "userId"),
    cart: a.hasOne("Cart", "userId")
  }).identifier(["email"]),

  UserRating: a.model({
    id: a.id().required(),
    userId: a.string().required(),
    user: a.belongsTo("User", "userId"),
    ratedId: a.string().required(),
    ratedUser: a.belongsTo("User", "ratedId"),
    rating: a.integer().required(),
    description: a.string(),
  }),

  Category: a.model({
    id: a.id().required(),
    name: a.string().required(),
    books: a.hasMany("BookCategory", "categoryId"),
  }),

  BookRating: a.model({
    id: a.id().required(),
    bookId: a.string().required(),
    book: a.belongsTo("Book", "bookId"),
    rating: a.integer().required(),
    description: a.string(),
  }),

  Listing: a.model({
    id: a.id().required(),
    bookId: a.string().required(),
    book: a.belongsTo("Book", "bookId"),
    userId: a.string().required(),
    user: a.belongsTo("User", "userId"),
    price: a.float().required(),
    status: a.enum(["available", "unavailable", "sold"]),
    photos: a.string().array().required(),
    cartId: a.id(),
    cart: a.belongsTo("Cart", "cartId")
  }),

  Cart: a.model({
    id: a.id().required(),
    userId: a.string().required(),
    user: a.belongsTo("User", "userId"),
    listings: a.hasMany("Listing", "cartId"),
    state: a.enum(["active", "completed", "shipping"]),
  }),
}).authorization((allow) => [allow.owner()]);

export type Schema = ClientSchema<typeof schema>;

export const data = defineData({
  schema,
  authorizationModes: {
    defaultAuthorizationMode: "userPool",
    // API Key is used for a.allow.public() rules
    apiKeyAuthorizationMode: {
      expiresInDays: 30,
    },
  },
});

/*== STEP 2 ===============================================================
Go to your frontend source code. From your client-side code, generate a
Data client to make CRUDL requests to your table. (THIS SNIPPET WILL ONLY
WORK IN THE FRONTEND CODE FILE.)

Using JavaScript or Next.js React Server Components, Middleware, Server 
Actions or Pages Router? Review how to generate Data clients for those use
cases: https://docs.amplify.aws/gen2/build-a-backend/data/connect-to-API/
=========================================================================*/

/*
"use client"
import { generateClient } from "aws-amplify/data";
import type { Schema } from "@/amplify/data/resource";

const client = generateClient<Schema>() // use this Data client for CRUDL requests
*/

/*== STEP 3 ===============================================================
Fetch records from the database and use them in your frontend component.
(THIS SNIPPET WILL ONLY WORK IN THE FRONTEND CODE FILE.)
=========================================================================*/

/* For example, in a React component, you can use this snippet in your
  function's RETURN statement */
// const { data: todos } = await client.models.Todo.list()

// return <ul>{todos.map(todo => <li key={todo.id}>{todo.content}</li>)}</ul>
