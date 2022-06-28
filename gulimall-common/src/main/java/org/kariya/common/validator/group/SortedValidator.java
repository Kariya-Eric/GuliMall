package org.kariya.common.validator.group;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

//自定义校验器
public class SortedValidator implements ConstraintValidator<SortValid,Integer> {

    private Set<Integer> set=new HashSet<>();
    //初始化
    @Override
    public void initialize(SortValid constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for(int i=0;i< vals.length;i++){
            set.add(vals[i]);
        }
    }

    //判断是否校验成功
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
