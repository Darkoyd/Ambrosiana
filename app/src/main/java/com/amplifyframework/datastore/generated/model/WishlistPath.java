package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the Wishlist type in your schema. */
public final class WishlistPath extends ModelPath<Wishlist> {
  private UserPath user;
  private BookWishlistPath books;
  WishlistPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, Wishlist.class);
  }
  
  public synchronized UserPath getUser() {
    if (user == null) {
      user = new UserPath("user", false, this);
    }
    return user;
  }
  
  public synchronized BookWishlistPath getBooks() {
    if (books == null) {
      books = new BookWishlistPath("books", true, this);
    }
    return books;
  }
}
