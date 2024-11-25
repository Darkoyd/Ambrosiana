package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
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

/** This is an auto generated class representing the Cart type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Carts", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE })
}, hasLazySupport = true)
@Index(name = "undefined", fields = {"id"})
public final class Cart implements Model {
  public static final CartPath rootPath = new CartPath("root", false, null);
  public static final QueryField ID = field("Cart", "id");
  public static final QueryField USER = field("Cart", "userId");
  public static final QueryField STATE = field("Cart", "state");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userId", targetNames = {"userId"}, type = User.class) ModelReference<User> user;
  private final @ModelField(targetType="Listing") @HasMany(associatedWith = "cart", type = Listing.class) ModelList<Listing> listings = null;
  private final @ModelField(targetType="CartState") CartState state;
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
  
  public ModelReference<User> getUser() {
      return user;
  }
  
  public ModelList<Listing> getListings() {
      return listings;
  }
  
  public CartState getState() {
      return state;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Cart(String id, ModelReference<User> user, CartState state) {
    this.id = id;
    this.user = user;
    this.state = state;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Cart cart = (Cart) obj;
      return ObjectsCompat.equals(getId(), cart.getId()) &&
              ObjectsCompat.equals(getUser(), cart.getUser()) &&
              ObjectsCompat.equals(getState(), cart.getState()) &&
              ObjectsCompat.equals(getCreatedAt(), cart.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), cart.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUser())
      .append(getState())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Cart {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("state=" + String.valueOf(getState()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
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
  public static Cart justId(String id) {
    return new Cart(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      user,
      state);
  }
  public interface BuildStep {
    Cart build();
    BuildStep id(String id);
    BuildStep user(User user);
    BuildStep state(CartState state);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private ModelReference<User> user;
    private CartState state;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<User> user, CartState state) {
      this.id = id;
      this.user = user;
      this.state = state;
    }
    
    @Override
     public Cart build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Cart(
          id,
          user,
          state);
    }
    
    @Override
     public BuildStep user(User user) {
        this.user = new LoadedModelReferenceImpl<>(user);
        return this;
    }
    
    @Override
     public BuildStep state(CartState state) {
        this.state = state;
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
    private CopyOfBuilder(String id, ModelReference<User> user, CartState state) {
      super(id, user, state);
      
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder state(CartState state) {
      return (CopyOfBuilder) super.state(state);
    }
  }
  

  public static class CartIdentifier extends ModelIdentifier<Cart> {
    private static final long serialVersionUID = 1L;
    public CartIdentifier(String id) {
      super(id);
    }
  }
  
}
