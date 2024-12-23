package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;

@Getter
@Setter
@Entity
@Table(name = "spend")
public class SpendEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
  private UUID id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CurrencyValues currency;

  @Column(name = "spend_date", columnDefinition = "DATE", nullable = false)
  private Date spendDate;

  @Column(nullable = false)
  private Double amount;

  @Column(nullable = false)
  private String description;

  @OneToOne(fetch = FetchType.EAGER, cascade = PERSIST)
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private CategoryEntity category;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    SpendEntity that = (SpendEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
  public static SpendEntity fromJson(@Nonnull SpendJson json) {
    SpendEntity se = new SpendEntity();
    se.setId(json.id());
    se.setUsername(json.username());
    se.setCurrency(json.currency());
    se.setSpendDate(new java.sql.Date(json.spendDate().getTime()));
    se.setAmount(json.amount());
    se.setDescription(json.description());
    se.setCategory(
            CategoryEntity.fromJson(
                    json.category()
            )
    );
    return se;
  }
}
