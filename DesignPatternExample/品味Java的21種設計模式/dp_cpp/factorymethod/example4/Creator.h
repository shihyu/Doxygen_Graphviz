#pragma once

#include "Product.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace factorymethod
			{
				namespace example4
				{
					///
					/// <summary> * �ͻ���ʹ��Creator���������£�Creator�Ļ���ʵ�ֽṹ </summary>
					/// 
					class Creator
					{
					///	
					///	 <summary> * ����������һ�㲻���� </summary>
					///	 * <returns> �����Ĳ�Ʒ���� </returns>
					///	 
					protected:
						virtual Product *factoryMethod() = 0;
					///	
					///	 <summary> * �ṩ���ⲿʹ�õķ�����
					///	 * �ͻ���һ��ʹ��Creator�ṩ����Щ�������������Ҫ�Ĺ��� </summary>
					///	 
					public:
						virtual void someOperation();
					};

				}
			}
		}
	}
}