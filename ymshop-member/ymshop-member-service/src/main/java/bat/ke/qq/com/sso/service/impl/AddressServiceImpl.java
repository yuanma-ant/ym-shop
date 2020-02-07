package bat.ke.qq.com.sso.service.impl;

import bat.ke.qq.com.common.exception.YmshopException;
import bat.ke.qq.com.manager.mapper.TbAddressMapper;
import bat.ke.qq.com.manager.pojo.TbAddress;
import bat.ke.qq.com.manager.pojo.TbAddressExample;
import bat.ke.qq.com.sso.service.AddressService;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 源码学院
 */
@Service
public class AddressServiceImpl implements AddressService {

    private static byte[] aesKey =
            SecureUtil.generateKey("PBE","ymshop666".getBytes()).getEncoded();
    private static String asterisk="****";

    private static SymmetricCrypto des =new DES(aesKey);
    @Autowired
    private TbAddressMapper tbAddressMapper;

    @Override
    public List<TbAddress> getAddressList(Long userId) {

        List<TbAddress> list=new ArrayList<>();
        TbAddressExample example=new TbAddressExample();
        TbAddressExample.Criteria criteria=example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        list=tbAddressMapper.selectByExample(example);

        if(list==null){
            throw new YmshopException("获取默认地址列表失败");
        }

        for(int i=0;i<list.size();i++){
            //电话号码加密  中间4位加 *
            TbAddress tbAddress=list.get(i);
            tbAddress.setTel(alertAsterisk(new String(des.decryptFromBase64(tbAddress.getTel()))));
            if(list.get(i).getIsDefault()){
                Collections.swap(list,0,i);
                break;
            }
        }

        return list;
    }

    @Override
    public TbAddress getAddress(Long addressId) {

        TbAddress tbAddress=tbAddressMapper.selectByPrimaryKey(addressId);
        if(tbAddress==null){
            throw new YmshopException("通过id获取地址失败");
        }
        //电话号码解密  中间4位加 *
        tbAddress.setTel(alertAsterisk(new String(des.decryptFromBase64(tbAddress.getTel()))));
        return tbAddress;
    }

    @Override
    public int addAddress(TbAddress tbAddress) {
        //电话号码加密
        tbAddress.setTel(des.encryptBase64(tbAddress.getTel()));
        //设置唯一默认
        setOneDefault(tbAddress);
        if(tbAddressMapper.insert(tbAddress)!=1){
            throw new YmshopException("添加地址失败");
        }
        return 1;
    }

    @Override
    public int updateAddress(TbAddress tbAddress) {
        if(tbAddress.getTel().contains(asterisk)){
            //如果电话号码的中间4位为 *  则不修改当前记录的电话号码
            tbAddress.setTel(null);
        }else{
            //加密后保存
            tbAddress.setTel(des.encryptBase64(tbAddress.getTel()));
        }
        //设置唯一默认
        setOneDefault(tbAddress);
        if(tbAddressMapper.updateByPrimaryKey(tbAddress)!=1){
            throw new YmshopException("更新地址失败");
        }
        return 1;
    }

    @Override
    public int delAddress(TbAddress tbAddress) {

        if(tbAddressMapper.deleteByPrimaryKey(tbAddress.getAddressId())!=1){
            throw new YmshopException("删除地址失败");
        }
        return 1;
    }

    public void setOneDefault(TbAddress tbAddress){
        //设置唯一默认
        if(tbAddress.getIsDefault()){
            TbAddressExample example=new TbAddressExample();
            TbAddressExample.Criteria criteria= example.createCriteria();
            criteria.andUserIdEqualTo(tbAddress.getUserId());
            List<TbAddress> list=tbAddressMapper.selectByExample(example);
            for(TbAddress tbAddress1:list){
                tbAddress1.setIsDefault(false);
                tbAddressMapper.updateByPrimaryKey(tbAddress1);
            }
        }
    }

    //手机号码中间4位加星
    private String alertAsterisk(String tel){
        String step1=tel.substring(0,3);
        String step2=tel.substring(7,11);
        return step1+"****"+step2;
    }
}
