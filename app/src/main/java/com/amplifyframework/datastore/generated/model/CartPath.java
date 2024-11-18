package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the Cart type in your schema. */
public final class CartPath extends ModelPath<Cart> {
  private UserPath user;
  private ListingPath listings;
  CartPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, Cart.class);
  }
  
  public synchronized UserPath getUser() {
    if (user == null) {
      user = new UserPath("user", false, this);
    }
    return user;
  }
  
  public synchronized ListingPath getListings() {
    if (listings == null) {
      listings = new ListingPath("listings", true, this);
    }
    return listings;
  }
}
