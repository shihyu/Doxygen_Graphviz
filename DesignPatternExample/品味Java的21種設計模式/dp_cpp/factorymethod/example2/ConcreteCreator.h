#pragma once

#include "Creator.h"
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
				namespace example2
				{
					///
					/// <summary> * ����Ĵ�����ʵ�ֶ��� </summary>
					/// 
					class ConcreteCreator : public Creator
					{
					public:
						virtual Product *factoryMethod();
					};


				}
			}
		}
	}
}