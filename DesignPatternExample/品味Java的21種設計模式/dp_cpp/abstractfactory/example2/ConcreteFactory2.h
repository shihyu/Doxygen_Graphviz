#pragma once

#include "AbstractFactory.h"
#include "AbstractProductA.h"
#include "ProductA2.h"
#include "AbstractProductB.h"
#include "ProductB2.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example2
				{
					///
					/// <summary> * ����Ĺ���ʵ�ֶ���ʵ�ִ�������Ĳ�Ʒ����Ĳ��� </summary>
					/// 
					class ConcreteFactory2 : public AbstractFactory
					{

					public:
						virtual AbstractProductA *createProductA();
						virtual AbstractProductB *createProductB();

					};


				}
			}
		}
	}
}