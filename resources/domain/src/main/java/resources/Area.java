package resources;


import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * Created by yangdongfeng on 2015/11/21.
 */
@Entity
@Table(name = "TR_AREA")
public class Area extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
