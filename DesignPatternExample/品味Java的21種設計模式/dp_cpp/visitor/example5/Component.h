#pragma once

#include "Visitor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example5
				{


					///
					/// <summary> * �������������൱�ڷ�����ģʽ�е�Ԫ�ض��� </summary>
					/// 
					class Component
					{
					///	
					///	 <summary> * ���ܷ����ߵķ��� </summary>
					///	 * <param name="visitor"> �����߶��� </param>
					///	 
					public:
						virtual void accept(Visitor *visitor) = 0;

					///	
					///	 <summary> * ����϶����м����������  </summary>
					///	 * <param name="child"> ��������϶����е�������� </param>
					///	 
						virtual void addChild(Component *child);
					///	
					///	 <summary> * ����϶������Ƴ�ĳ��������� </summary>
					///	 * <param name="child"> ���Ƴ���������� </param>
					///	 
						virtual void removeChild(Component *child);
					///	
					///	 <summary> * ����ĳ��������Ӧ��������� </summary>
					///	 * <param name="index"> ��Ҫ��ȡ����������������������0��ʼ </param>
					///	 * <returns> ������Ӧ��������� </returns>
					///	 
						virtual Component *getChildren(int index);
					};


				}
			}
		}
	}
}