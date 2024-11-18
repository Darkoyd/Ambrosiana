package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.ModelReference;
import com.amplifyframework.core.model.LoadedModelReferenceImpl;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Listing type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Listings", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
}, hasLazySupport = true)
@Index(name = "undefined", fields = {"id"})
public final class Listing implements Model {
  public static final ListingPath rootPath = new ListingPath("root", false, null);
  public static final QueryField ID = field("Listing", "id");
  public static final QueryField BOOK = field("Listing", "bookId");
  public static final QueryField USER = field("Listing", "userId");
  public static final QueryField PRICE = field("Listing", "price");
  public static final QueryField STATUS = field("Listing", "status");
  public static final QueryField PHOTOS = field("Listing", "photos");
  public static final QueryField CART = field("Listing", "cartId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Book") @BelongsTo(targetName = "bookId", targetNames = {"bookId"}, type = Book.class) ModelReference<Book> book;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userId", targetNames = {"userId"}, type = User.class) ModelReference<User> user;
  private final @ModelField(targetType="Float", isRequired = true) Double price;
  private final @ModelField(targetType="ListingStatus") ListingStatus status;
  private final @ModelField(targetType="String", isRequired = true) List<String> photos;
  private final @ModelField(targetType="Cart") @BelongsTo(targetName = "cartId", targetNames = {"cartId"}, type = Cart.class) ModelReference<Cart> cart;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public ModelReference<Book> getBook() {
      return book;
  }
  
  public ModelReference<User> getUser() {
      return user;
  }
  
  public Double getPrice() {
      return price;
  }
  
  public ListingStatus getStatus() {
      return status;
  }
  
  public List<String> getPhotos() {
      return photos;
  }
  
  public ModelReference<Cart> getCart() {
      return cart;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Listing(String id, ModelReference<Book> book, ModelReference<User> user, Double price, ListingStatus status, List<String> photos, ModelReference<Cart> cart) {
    this.id = id;
    this.book = book;
    this.user = user;
    this.price = price;
    this.status = status;
    this.photos = photos;
    this.cart = cart;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Listing listing = (Listing) obj;
      return ObjectsCompat.equals(getId(), listing.getId()) &&
              ObjectsCompat.equals(getBook(), listing.getBook()) &&
              ObjectsCompat.equals(getUser(), listing.getUser()) &&
              ObjectsCompat.equals(getPrice(), listing.getPrice()) &&
              ObjectsCompat.equals(getStatus(), listing.getStatus()) &&
              ObjectsCompat.equals(getPhotos(), listing.getPhotos()) &&
              ObjectsCompat.equals(getCart(), listing.getCart()) &&
              ObjectsCompat.equals(getCreatedAt(), listing.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), listing.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBook())
      .append(getUser())
      .append(getPrice())
      .append(getStatus())
      .append(getPhotos())
      .append(getCart())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Listing {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("book=" + String.valueOf(getBook()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("price=" + String.valueOf(getPrice()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("photos=" + String.valueOf(getPhotos()) + ", ")
      .append("cart=" + String.valueOf(getCart()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static PriceStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Listing justId(String id) {
    return new Listing(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      book,
      user,
      price,
      status,
      photos,
      cart);
  }
  public interface PriceStep {
    PhotosStep price(Double price);
  }
  

  public interface PhotosStep {
    BuildStep photos(List<String> photos);
  }
  

  public interface BuildStep {
    Listing build();
    BuildStep id(String id);
    BuildStep book(Book book);
    BuildStep user(User user);
    BuildStep status(ListingStatus status);
    BuildStep cart(Cart cart);
  }
  

  public static class Builder implements PriceStep, PhotosStep, BuildStep {
    private String id;
    private Double price;
    private List<String> photos;
    private ModelReference<Book> book;
    private ModelReference<User> user;
    private ListingStatus status;
    private ModelReference<Cart> cart;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<Book> book, ModelReference<User> user, Double price, ListingStatus status, List<String> photos, ModelReference<Cart> cart) {
      this.id = id;
      this.book = book;
      this.user = user;
      this.price = price;
      this.status = status;
      this.photos = photos;
      this.cart = cart;
    }
    
    @Override
     public Listing build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Listing(
          id,
          book,
          user,
          price,
          status,
          photos,
          cart);
    }
    
    @Override
     public PhotosStep price(Double price) {
        Objects.requireNonNull(price);
        this.price = price;
        return this;
    }
    
    @Override
     public BuildStep photos(List<String> photos) {
        Objects.requireNonNull(photos);
        this.photos = photos;
        return this;
    }
    
    @Override
     public BuildStep book(Book book) {
        this.book = new LoadedModelReferenceImpl<>(book);
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.user = new LoadedModelReferenceImpl<>(user);
        return this;
    }
    
    @Override
     public BuildStep status(ListingStatus status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep cart(Cart cart) {
        this.cart = new LoadedModelReferenceImpl<>(cart);
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, ModelReference<Book> book, ModelReference<User> user, Double price, ListingStatus status, List<String> photos, ModelReference<Cart> cart) {
      super(id, book, user, price, status, photos, cart);
      Objects.requireNonNull(price);
      Objects.requireNonNull(photos);
    }
    
    @Override
     public CopyOfBuilder price(Double price) {
      return (CopyOfBuilder) super.price(price);
    }
    
    @Override
     public CopyOfBuilder photos(List<String> photos) {
      return (CopyOfBuilder) super.photos(photos);
    }
    
    @Override
     public CopyOfBuilder book(Book book) {
      return (CopyOfBuilder) super.book(book);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder status(ListingStatus status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder cart(Cart cart) {
      return (CopyOfBuilder) super.cart(cart);
    }
  }
  

  public static class ListingIdentifier extends ModelIdentifier<Listing> {
    private static final long serialVersionUID = 1L;
    public ListingIdentifier(String id) {
      super(id);
    }
  }
  
}
