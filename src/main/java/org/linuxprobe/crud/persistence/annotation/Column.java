package org.linuxprobe.crud.persistence.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 列名标注 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	/** 列名 */
	public String value() default "";

	/** 忽略更新,生成的update sql 语句将不包含该字段 */
	public boolean updateIgnore() default false;
}
