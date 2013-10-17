#pragma once

#include "Product1.h"
#include "Product2.h"
#include "Product.h"
#include "ConcreteProduct.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace factorymethod
			{
				namespace example5
				{
					///
					/// <summary> * �ͻ���ʹ��Creator�������ͻ�����Ҫ�Ķ��������£�Creator�Ļ���ʵ�ֽṹ </summary>
					/// 
					class Creator
					{
					///	
					///	 <summary> * ����������һ�㲻���⣬����һ���������� </summary>
					///	 * <returns> �����Ĳ�Ʒ����һ������һ����Ʒ����Ĳ��� </returns>
					///	 
					protected:
						virtual Product1 *factoryMethod1() = 0;
					///	
					///	 <summary> * ����������һ�㲻���⣬����һ���������� </summary>
					///	 * <returns> �����Ĳ�Ʒ����һ������һ����Ʒ����Ĳ��� </returns>
					///	 
						virtual Product2 *factoryMethod2() = 0;
					///	
					///	 <summary> * �����ͻ�����Ҫ�Ķ��󣬿ͻ�����Ҫʹ�ò�Ʒ�������������Ҫ�Ĺ��� </summary>
					///	 * <returns> �ͻ�����Ҫ�Ķ��� </returns>
					///	 
					public:
						virtual Product *createProduct();
					};

				}
			}
		}
	}
}