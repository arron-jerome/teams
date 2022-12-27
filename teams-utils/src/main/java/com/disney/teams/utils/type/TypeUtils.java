package com.disney.teams.utils.type;

import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;
import sun.reflect.generics.tree.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class TypeUtils {

    public static interface TypeParser {

        boolean canParse(Object obj, Type type);

        <T> T parse(Object src, Type type);

    }

    private static final List<TypeParser> parsers = new ArrayList<>();

    public static void addParse(TypeParser parse) {
        parsers.add(parse);
    }

    private static TypeParser typeParser(Object obj, Type type) {
        if(obj == null) {
            return null;
        }
        for (TypeParser parser : parsers) {
            if(parser.canParse(obj, type)) {
                return parser;
            }
        }
        return null;
    }

    public static boolean isAllClass(Type[] types) {
        if (ArrayUtils.isEmpty(types)) {
            return true;
        }
        for (Type type : types) {
            if (!(type instanceof Class)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Class<T> toClass(Type type) {
        if (type == null) {
            return null;
        } else if (type instanceof Class) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        } else {
            return (Class<T>) type.getClass();
        }
    }

    public static ParameterizedType getRealType(ParameterizedType type, Map<Type, Type> typeMap) {
        Type[] types = type.getActualTypeArguments();
        if (TypeUtils.isAllClass(types)) {
            return type;
        }
        types = getRealType(types, typeMap);
        Type rawType = type.getRawType();
        return ParameterizedTypeImpl.make((Class<?>) rawType, types, null);
    }

    public static GenericArrayType getRealType(GenericArrayType type, Map<Type, Type> typeMap) {
        Type ctype = type.getGenericComponentType();
        if (ctype instanceof Class) {
            return type;
        }
        Type realType = typeMap.get(ctype);
        if (realType == null) {
            realType = getRealType(ctype, typeMap);
        }
        return GenericArrayTypeImpl.make(realType);
    }

    public static FieldTypeSignature[] makeFieldTypeSignature(Type[] types) {
        if (ArrayUtils.isEmpty(types)) {
            return null;
        }
        FieldTypeSignature[] fieldTypeSignatures = new FieldTypeSignature[types.length];
        for (int i = 0; i < types.length; i++) {
            fieldTypeSignatures[i] = makeFieldTypeSignature(types[i]);
        }
        return fieldTypeSignatures;
    }

    public static FieldTypeSignature makeFieldTypeSignature(Type type) {
        FieldTypeSignature fieldTypeSignature = null;
        if (type instanceof ParameterizedType){
            Type[] types = ((ParameterizedType)type).getActualTypeArguments();
            TypeArgument[] typeArguments = new TypeArgument[types.length];
            for(int i = 0; i < types.length; i++){
                typeArguments[i] = makeFieldTypeSignature(types[i]);
            }
            Type x = ((ParameterizedType)type).getRawType();
            SimpleClassTypeSignature simpleClassTypeSignature = SimpleClassTypeSignature.make(((Class)x).getName(), false, typeArguments);
            ArrayList<SimpleClassTypeSignature> arrayList = new ArrayList<>();
            arrayList.add(0,simpleClassTypeSignature);
            fieldTypeSignature = ClassTypeSignature.make(arrayList);
        }
        else if (type instanceof TypeVariable) {
            TypeVariableSignature typeVariableSignature = TypeVariableSignature.make(((TypeVariable) type).getName());
            return typeVariableSignature;
        }
        else if (type instanceof GenericArrayType) {
            Type type1 = ((GenericArrayType)type).getGenericComponentType();
            TypeSignature typeSignature  = makeFieldTypeSignature(type1);
            ArrayTypeSignature typeVariableSignature = ArrayTypeSignature.make(typeSignature);
            return typeVariableSignature;
        }
        else if (type instanceof Class) {
            if(((Class)type).isArray()){
                SimpleClassTypeSignature fieldTypeSignature1 = SimpleClassTypeSignature.make(((Class)type).getComponentType().getName() , false, new TypeArgument[0]);
                ArrayList<SimpleClassTypeSignature> arrayList = new ArrayList<>();
                arrayList.add(0,fieldTypeSignature1);
                ClassTypeSignature fieldTypeSignature2 = ClassTypeSignature.make(arrayList);
                return ArrayTypeSignature.make(fieldTypeSignature2);
            }else{
                SimpleClassTypeSignature fieldTypeSignature1 = SimpleClassTypeSignature.make(((Class) type).getName(), false, new TypeArgument[0]);
                return fieldTypeSignature1;
            }
        }
        return fieldTypeSignature;
    }

    public static Type getRealType(WildcardType type, Map<Type, Type> typeMap) {
        GenericsFactory factory = MethodUtils.invokeGetterMethod(type, "factory");
        Type[] cuType = type.getUpperBounds();
        Type[] clType = type.getLowerBounds();
        if (ArrayUtils.isNotEmpty(cuType)) {
            if (TypeUtils.isAllClass(cuType))
                return type;
            FieldTypeSignature[] fieldTypeSignatures = new FieldTypeSignature[cuType.length];
            for(int i = 0; i < cuType.length; i++){
                fieldTypeSignatures[i] = makeFieldTypeSignature(getRealType(cuType[i], typeMap));
            }
           return WildcardTypeImpl.make(fieldTypeSignatures,new FieldTypeSignature[0],factory);
        }else if (ArrayUtils.isNotEmpty(clType)) {
            if (TypeUtils.isAllClass(clType))
                return type;
            FieldTypeSignature[] fieldTypeSignatures = new FieldTypeSignature[clType.length];
            for(int i = 0; i < clType.length; i++){
                fieldTypeSignatures[i] = makeFieldTypeSignature(getRealType(clType[i], typeMap));
            }
            return WildcardTypeImpl.make(new FieldTypeSignature[0],fieldTypeSignatures,factory);
        }
        return type;
    }

    public static Type getRealType(TypeVariable type, Map<Type, Type> typeMap) {
        if (typeMap == null) {
            return type;
        }
        Type realType = typeMap.get(type);
        if (realType == null || realType == type) {
            return type;
        }
        return getRealType(realType, typeMap);
    }

    public static Type getRealType(Type type, Map<Type, Type> typeMap) {
        Type realType = (MapUtils.isEmpty(typeMap) ? type : typeMap.get(type));
        if (realType instanceof Class) {
            return realType;
        }
        realType = (realType == null) ? type : realType;
        if (realType == null) {
            return null;
        }
        if (realType instanceof ParameterizedType) {
            realType = getRealType((ParameterizedType) realType, typeMap);
        } else if (realType instanceof GenericArrayType) {
            realType = getRealType((GenericArrayType) realType, typeMap);
        } else if (realType instanceof WildcardType) {
            realType = getRealType((WildcardType) realType, typeMap);
        } else if (realType instanceof TypeVariable) {
            realType = getRealType((TypeVariable) realType, typeMap);
        }
        return realType;
    }

    public static Type[] getRealType(Type[] types, Map<Type, Type> typeMap) {
        if (ArrayUtils.isEmpty(types)) {
            return types;
        }
        for (int i = 0, len = types.length; i < len; ++i) {
            types[i] = getRealType(types[i], typeMap);
        }
        return types;
    }

    public static <T> T convert(Object src, WildcardType targetType) {
        TypeParser parser = typeParser(src, targetType);
        if(parser != null) {
            return parser.parse(src, targetType);
        }

        Type[] bounds = targetType.getUpperBounds();
        return ClassUtils.convert(src, (Class<T>) bounds[0]);
    }

    public static <T> T convert(Object src, TypeVariable targetType) {
        TypeParser parser = typeParser(src, targetType);
        if(parser != null) {
            return parser.parse(src, targetType);
        }

        Type[] bounds = targetType.getBounds();
        return ClassUtils.convert(src, (Class<T>) bounds[0]);
    }

    public static <T> T convert(Object src, Type targetType) {
        TypeParser parser = typeParser(src, targetType);
        if(parser != null) {
            return parser.parse(src, targetType);
        }

        if (targetType == null) {
            return (T) src;
        }
        if (targetType instanceof Class) {
            return ClassUtils.convert(src, (Class<T>) targetType);
        } else if (targetType instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) targetType;
            Class<T> clz = (Class<T>) ptype.getRawType();
            Type[] types = ptype.getActualTypeArguments();
            return ClassUtils.convert(src, clz, types);
        } else if (targetType instanceof GenericArrayType) {
            return ArrayUtils.convert(src, targetType);
        } else if (targetType instanceof WildcardType) {
            return convert(src, (WildcardType) targetType);
        } else if (targetType instanceof TypeVariable) {
            return convert(src, (TypeVariable) targetType);
        } else {
            throw new UnsupportedOperationException(targetType + " is not support!");
        }
    }
}
