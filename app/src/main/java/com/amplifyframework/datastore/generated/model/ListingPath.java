package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the Listing type in your schema. */
public final class ListingPath extends ModelPath<Listing> {
  private BookPath book;
  private UserPath user;
  private CartPath cart;
  ListingPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, Listing.class);
  }
  
  public synchronized BookPath getBook() {
    if (book == null) {
      book = new BookPath("book", false, this);
    }
    return book;
  }
  
  public synchronized UserPath getUser() {
    if (user == null) {
      user = new UserPath("user", false, this);
    }
    return user;
  }
  
  public synchronized CartPath getCart() {
    if (cart == null) {
      cart = new CartPath("cart", false, this);
    }
    return cart;
  }
}
