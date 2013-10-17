#pragma once

#include "Element.h"
#include "Visitor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example3
				{
					///
					/// <summary> * ����ṹ,ͨ���������Ԫ�ض�����б������÷������ܷ��ʵ����е�Ԫ�� </summary>
					/// 
					class ObjectStructure
					{
					///	
					///	 <summary> * ʾ�⣬��ʾ����ṹ��������һ����Ͻṹ���Ǽ��� </summary>
					///	 
					private:
						Collection<Element*> *col;
					///	
					///	 <summary> * ʾ�ⷽ�����ṩ���ͻ��˲����ĸ߲�ӿ� </summary>
					///	 * <param name="visitor"> �ͻ�����Ҫʹ�õķ����� </param>
					///	 
					public:
						virtual void handleRequest(Visitor *visitor);
					///	
					///	 <summary> * ʾ�ⷽ�����齨����ṹ�������ṹ�����Ԫ�ء�
					///	 * ��ͬ�Ķ���ṹ�в�ͬ�Ĺ�����ʽ </summary>
					///	 * <param name="ele"> ���뵽����ṹ��Ԫ�� </param>
					///	 
						virtual void addElement(Element *ele);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								col = std::vector<Element*>();

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