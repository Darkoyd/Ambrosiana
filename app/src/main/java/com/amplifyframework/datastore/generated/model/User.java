package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasOne;
import com.amplifyframework.core.model.ModelReference;
import com.amplifyframework.core.model.LoadedModelReferenceImpl;
import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.ModelList;
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

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
}, hasLazySupport = true)
@Index(name = "undefined", fields = {"email"})
public final class User implements Model {
  public static final UserPath rootPath = new UserPath("root", false, null);
  public static final QueryField EMAIL = field("User", "email");
  public static final QueryField FIRST_NAME = field("User", "firstName");
  public static final QueryField LAST_NAME = field("User", "lastName");
  public static final QueryField ADDRESS = field("User", "address");
  public static final QueryField PHONE = field("User", "phone");
  private final @ModelField(targetType="String", isRequired = true) String email;
  private final @ModelField(targetType="String") String firstName;
  private final @ModelField(targetType="String") String lastName;
  private final @ModelField(targetType="String") String address;
  private final @ModelField(targetType="String") String phone;
  private final @ModelField(targetType="UserLibrary") @HasOne(associatedWith = "user", type = UserLibrary.class) ModelReference<UserLibrary> library = null;
  private final @ModelField(targetType="UserRating") @HasMany(associatedWith = "ratedUser", type = UserRating.class) ModelList<UserRating> ratingsReceived = null;
  private final @ModelField(targetType="UserRating") @HasMany(associatedWith = "user", type = UserRating.class) ModelList<UserRating> ratings = null;
  private final @ModelField(targetType="Listing") @HasMany(associatedWith = "user", type = Listing.class) ModelList<Listing> listings = null;
  private final @ModelField(targetType="Wishlist") @HasOne(associatedWith = "user", type = Wishlist.class) ModelReference<Wishlist> wishlist = null;
  private final @ModelField(targetType="Cart") @HasOne(associatedWith = "user", type = Cart.class) ModelReference<Cart> cart = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return email;
  }
  
  public String getEmail() {
      return email;
  }
  
  public String getFirstName() {
      return firstName;
  }
  
  public String getLastName() {
      return lastName;
  }
  
  public String getAddress() {
      return address;
  }
  
  public String getPhone() {
      return phone;
  }
  
  public ModelReference<UserLibrary> getLibrary() {
      return library;
  }
  
  public ModelList<UserRating> getRatingsReceived() {
      return ratingsReceived;
  }
  
  public ModelList<UserRating> getRatings() {
      return ratings;
  }
  
  public ModelList<Listing> getListings() {
      return listings;
  }
  
  public ModelReference<Wishlist> getWishlist() {
      return wishlist;
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
  
  private User(String email, String firstName, String lastName, String address, String phone) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phone = phone;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getEmail(), user.getEmail()) &&
              ObjectsCompat.equals(getFirstName(), user.getFirstName()) &&
              ObjectsCompat.equals(getLastName(), user.getLastName()) &&
              ObjectsCompat.equals(getAddress(), user.getAddress()) &&
              ObjectsCompat.equals(getPhone(), user.getPhone()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getEmail())
      .append(getFirstName())
      .append(getLastName())
      .append(getAddress())
      .append(getPhone())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("firstName=" + String.valueOf(getFirstName()) + ", ")
      .append("lastName=" + String.valueOf(getLastName()) + ", ")
      .append("address=" + String.valueOf(getAddress()) + ", ")
      .append("phone=" + String.valueOf(getPhone()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static EmailStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(email,
      firstName,
      lastName,
      address,
      phone);
  }
  public interface EmailStep {
    BuildStep email(String email);
  }
  

  public interface BuildStep {
    User build();
    BuildStep firstName(String firstName);
    BuildStep lastName(String lastName);
    BuildStep address(String address);
    BuildStep phone(String phone);
  }
  

  public static class Builder implements EmailStep, BuildStep {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    public Builder() {
      
    }
    
    private Builder(String email, String firstName, String lastName, String address, String phone) {
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.phone = phone;
    }
    
    @Override
     public User build() {
        
        return new User(
          email,
          firstName,
          lastName,
          address,
          phone);
    }
    
    @Override
     public BuildStep email(String email) {
        Objects.requireNonNull(email);
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    @Override
     public BuildStep lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    @Override
     public BuildStep address(String address) {
        this.address = address;
        return this;
    }
    
    @Override
     public BuildStep phone(String phone) {
        this.phone = phone;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String email, String firstName, String lastName, String address, String phone) {
      super(email, firstName, lastName, address, phone);
      Objects.requireNonNull(email);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder firstName(String firstName) {
      return (CopyOfBuilder) super.firstName(firstName);
    }
    
    @Override
     public CopyOfBuilder lastName(String lastName) {
      return (CopyOfBuilder) super.lastName(lastName);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
    
    @Override
     public CopyOfBuilder phone(String phone) {
      return (CopyOfBuilder) super.phone(phone);
    }
  }
  

  public static class UserIdentifier extends ModelIdentifier<User> {
    private static final long serialVersionUID = 1L;
    public UserIdentifier(String email) {
      super(email);
    }
  }
  
}
