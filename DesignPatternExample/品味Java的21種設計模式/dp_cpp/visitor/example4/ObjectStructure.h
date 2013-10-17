#pragma once

#include "Customer.h"
#include "Visitor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example4
				{
					///
					/// <summary> * ����ṹ,ͨ���������Ԫ�ض�����б������÷������ܷ��ʵ����е�Ԫ�� </summary>
					/// 
					class ObjectStructure
					{
					///	
					///	 <summary> * Ҫ�����Ŀͻ����� </summary>
					///	 
					private:
						Collection<Customer*> *col;
					///	
					///	 <summary> * �ṩ���ͻ��˲����ĸ߲�ӿڣ�����Ĺ����ɿͻ��˴���ķ����߾��� </summary>
					///	 * <param name="visitor"> �ͻ�����Ҫʹ�õķ����� </param>
					///	 
					public:
						virtual void handleRequest(Visitor *visitor);
					///	
					///	 <summary> * �齨����ṹ�������ṹ�����Ԫ�ء�
					///	 * ��ͬ�Ķ���ṹ�в�ͬ�Ĺ�����ʽ </summary>
					///	 * <param name="ele"> ���뵽����ṹ��Ԫ�� </param>
					///	 
						virtual void addElement(Customer *ele);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								col = std::vector<Customer*>();

								initialized = true;
							}
						}

public:
	ObjectStructure()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}