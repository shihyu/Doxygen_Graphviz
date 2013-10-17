#pragma once

#include "AbstractProductA.h"
#include "AbstractProductB.h"

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
					/// <summary> * ���󹤳��Ľӿڣ��������������Ʒ����Ĳ��� </summary>
					/// 
					class AbstractFactory
					{
					///	
					///	 <summary> * ʾ�����������������ƷA�Ķ��� </summary>
					///	 * <returns> �����ƷA�Ķ��� </returns>
					///	 
					public:
						virtual public AbstractProductA *createProductA() = 0;
					///	
					///	 <summary> * ʾ�����������������ƷB�Ķ��� </summary>
					///	 * <returns> �����ƷB�Ķ��� </returns>
					///	 
						virtual public AbstractProductB *createProductB() = 0;
					};
				}
			}
		}
	}
}