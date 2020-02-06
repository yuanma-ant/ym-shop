package bat.ke.qq.com.manager.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * tb_seckill
 * @author
 */
public class TbSeckill implements Serializable {
    private Long id;

    private Long itemId;

    private String seckillDesc;

    private BigDecimal seckillPrice;

    private Integer seckillStock;

    private Date startdate;

    private Date enddate;

    private Date created;

    private Date updated;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getSeckillDesc() {
        return seckillDesc;
    }

    public void setSeckillDesc(String seckillDesc) {
        this.seckillDesc = seckillDesc;
    }

    public BigDecimal getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
