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
				namespace example2
				{
					///
					/// <summary> * �������������������� </summary>
					/// 
					class Creator
					{
					///	
					///	 <summary> * ����Product�Ĺ������� </summary>
					///	 * <returns> Product���� </returns>
					///	 
					protected:
						virtual Product *factoryMethod() = 0;
					///	
					///	 <summary> * ʾ�ⷽ����ʵ��ĳЩ���ܵķ���  </summary>
					///	 
					public:
						virtual void someOperation();

					};


				}
			}
		}
	}
}