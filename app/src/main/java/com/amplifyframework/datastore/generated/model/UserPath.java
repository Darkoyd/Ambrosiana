package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the User type in your schema. */
public final class UserPath extends ModelPath<User> {
  private UserLibraryPath library;
  private UserRatingPath ratingsReceived;
  private UserRatingPath ratings;
  private ListingPath listings;
  private WishlistPath wishlist;
  private CartPath cart;
  UserPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, User.class);
  }
  
  public synchronized UserLibraryPath getLibrary() {
    if (library == null) {
      library = new UserLibraryPath("library", false, this);
    }
    return library;
  }
  
  public synchronized UserRatingPath getRatingsReceived() {
    if (ratingsReceived == null) {
      ratingsReceived = new UserRatingPath("ratingsReceived", true, this);
    }
    return ratingsReceived;
  }
  
  public synchronized UserRatingPath getRatings() {
    if (ratings == null) {
      ratings = new UserRatingPath("ratings", true, this);
    }
    return ratings;
  }
  
  public synchronized ListingPath getListings() {
    if (listings == null) {
      listings = new ListingPath("listings", true, this);
    }
    return listings;
  }
  
  public synchronized WishlistPath getWishlist() {
    if (wishlist == null) {
      wishlist = new WishlistPath("wishlist", false, this);
    }
    return wishlist;
  }
  
  public synchronized CartPath getCart() {
    if (cart == null) {
      cart = new CartPath("cart", false, this);
    }
    return cart;
  }
}
