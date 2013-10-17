#pragma once

#include "Component.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example2
				{


					///
					/// <summary> * ��϶���ͨ����Ҫ�洢�Ӷ��󣬶������Ӳ����Ĳ�����Ϊ��
					/// * ��ʵ����Component���涨������Ӳ����йصĲ��� </summary>
					/// 
					class Composite : public Component
					{
					///	
					///	 <summary> * �����洢��϶����а�������������� </summary>
					///	 
					private:
						std::vector<Component*> childComponents;

					///	
					///	 <summary> * ʾ�ⷽ����ͨ����������Ҫʵ�ֵݹ�ĵ��� </summary>
					///	 
					public:
						virtual void someOperation();
						virtual void addChild(Component *child);

						virtual void removeChild(Component *child);

						virtual Component *getChildren(int index);
					};


				}
			}
		}
	}
}